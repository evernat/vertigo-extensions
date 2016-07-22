/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2016, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.dynamo.impl.store.datastore.cache;

import io.vertigo.commons.cache.CacheConfig;
import io.vertigo.commons.cache.CacheManager;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtListURI;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Assertion;

/**
 * Gestion des données mises en cache.
 * Centralise la dépendance à CacheManager.
 *
 * @author  pchretien
 */
public final class CacheData {
	private final CacheManager cacheManager;

	/**
	 * Constructor.
	 * @param cacheManager Cache manager
	 */
	CacheData(final CacheManager cacheManager) {
		Assertion.checkNotNull(cacheManager);
		//-----
		this.cacheManager = cacheManager;
	}

	/**
	 * Register a context cache dedicated to one DtDefinition.
	 * @param dtDefinition DT definition
	 * @param timeToLiveSeconds Time to live in cache
	 * @param serializeElements Elements should be serialized to guarantee there aren't modified
	 */
	void registerContext(final DtDefinition dtDefinition, final long timeToLiveSeconds, final boolean serializeElements) {
		final String context = getContext(dtDefinition);
		final int maxElementsInMemory = 1000;
		final long timeToIdleSeconds = timeToLiveSeconds / 2; //longévité d'un élément non utilisé
		cacheManager.addCache(context, new CacheConfig("dataCache", serializeElements, maxElementsInMemory, timeToLiveSeconds, timeToIdleSeconds));
	}

	private static String getContext(final DtDefinition dtDefinition) {
		return "DataCache:" + dtDefinition.getName();
	}

	/**
	 * Récupération d'un objet potentiellement mis en cache
	 * @param uri URI du DTO
	 * @return null ou DTO
	 * @param <D> Dt type
	 */
	<D extends Entity> D getDtObject(final URI<D> uri) {
		final DtDefinition dtDefinition = uri.getDefinition();
		return (D) cacheManager.get(getContext(dtDefinition), uri);
	}

	/**
	 * Mise à jour du cache pour un type d'objet.
	 * @param dto DTO
	 */
	void putDtObject(final Entity dto) {
		Assertion.checkNotNull(dto);
		//-----
		final String context = getContext(DtObjectUtil.findDtDefinition(dto));
		//2.On met à jour l'objet
		cacheManager.put(context, DtObjectUtil.createURI(dto), dto);
	}

	/**
	 * Récupération de la liste ratine objet potentiellement mise en cache
	 * @param dtcUri URI de la DTC
	 * @return null ou DTC
	 * @param <D> Dt type
	 */
	<D extends DtObject> DtList<D> getDtList(final DtListURI dtcUri) {
		Assertion.checkNotNull(dtcUri);
		//-----
		return DtList.class.cast(cacheManager.get(getContext(dtcUri.getDtDefinition()), dtcUri));
	}

	/**
	 * Mise à jour du cache pour un type d'objet.
	 * @param dtc DTC
	 */
	void putDtList(final DtList<? extends Entity> dtc) {
		Assertion.checkNotNull(dtc);
		//-----
		final String context = getContext(dtc.getDefinition());

		//1.On met à jour les objets
		for (final Entity dto : dtc) {
			cacheManager.put(context, DtObjectUtil.createURI(dto), dto);
		}
		//2.Puis on met à jour la liste racine : pour que la liste ne soit pas evincée par les objets
		cacheManager.put(context, dtc.getURI(), dtc);
	}

	/**
	 * @param dtDefinition Dt definition to clear
	 */
	void clear(final DtDefinition dtDefinition) {
		Assertion.checkNotNull(dtDefinition);
		//-----
		cacheManager.clear(getContext(dtDefinition));
	}
}
