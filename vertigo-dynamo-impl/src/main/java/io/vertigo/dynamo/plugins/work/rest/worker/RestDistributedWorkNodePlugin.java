/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.dynamo.plugins.work.rest.worker;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.dynamo.impl.node.NodePlugin;
import io.vertigo.dynamo.impl.work.worker.local.LocalWorker;
import io.vertigo.dynamo.node.Node;
import io.vertigo.dynamo.work.WorkManager;
import io.vertigo.kernel.lang.Activeable;
import io.vertigo.kernel.lang.Assertion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

/**
 * Implémentation de DistributedWorkManager, pour l'execution de travaux par des Workers distant.
 * Cette implémentation représente la partie client qui se déploie en ferme.
 * 1- contacte la partie serveur pour récupérer les travaux qu'elle sait gérer,
 * 2- execute la tache en synchrone exclusivement
 * 3- retourne le résultat au serveur
 * 
 * @author npiedeloup, pchretien
 */
public final class RestDistributedWorkNodePlugin implements NodePlugin, Activeable {

	/** Log du workerNode. */
	final Logger logger = Logger.getLogger(RestDistributedWorkNodePlugin.class);

	private final List<String> workTypes;
	private final LocalWorker localWorker = new LocalWorker(/*workersCount*/5);

	private final WorkManager workManager;
	private final WorkQueueRestClient workQueueClient; //devrait etre un plugin

	private final String nodeId;
	private final List<Thread> dispatcherThreads = new ArrayList<>();

	/**
	 * Constructeur.
	 * @param nodeId Identifiant du noeud
	 * @param workTypes Types de travail gérés
	 * @param serverUrl Url du serveur
	 * @param workManager Manager des works
	 * @param codecManager Manager d'encodage/decodage
	 */
	@Inject
	public RestDistributedWorkNodePlugin(@Named("nodeId") final String nodeId, @Named("workTypes") final String workTypes, @Named("serverUrl") final String serverUrl, final WorkManager workManager, final CodecManager codecManager) {
		Assertion.checkArgNotEmpty(nodeId);
		Assertion.checkArgNotEmpty(workTypes);
		Assertion.checkArgNotEmpty(serverUrl);
		Assertion.checkNotNull(workManager);
		//---------------------------------------------------------------------
		this.nodeId = nodeId;
		this.workTypes = Arrays.asList(workTypes.trim().split(";"));
		this.workManager = workManager;
		workQueueClient = new WorkQueueRestClient(nodeId, serverUrl + "/workQueue", codecManager);
		//---
		for (final String workType : this.workTypes) {
			dispatcherThreads.add(new Thread(new PollWorkTask<>( workType, workQueueClient, localWorker)));
		}
	}

	/** {@inheritDoc} */
	public void start() {
		//1 pooler par type, pour éviter que l'attente lors du poll pour une file vide pénalise les autres
		for (final Thread dispatcherThread : dispatcherThreads) {
			dispatcherThread.start();
		}
	}

	/** {@inheritDoc} */
	public void stop() {
		for (final Thread dispatcherThread : dispatcherThreads) {
			dispatcherThread.interrupt();
		}
		for (final Thread dispatcherThread : dispatcherThreads) {
			try {
				dispatcherThread.join();
			} catch (final InterruptedException e) {
				//On ne fait rien
			}
		}
		localWorker.close();
	}

	//-------------------------------------------------------------------------
	// Methodes appelant la workQueue distribuée.
	// TODO: utiliser un plugin
	//-------------------------------------------------------------------------

	/** {@inheritDoc} */
	public List<Node> getNodes() {
		return Collections.singletonList(new Node(nodeId, true));
	}
}
