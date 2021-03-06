/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, vertigo-io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.stella;

import io.vertigo.app.config.Feature;
import io.vertigo.app.config.Features;
import io.vertigo.core.param.Param;
import io.vertigo.stella.impl.master.MasterManagerImpl;
import io.vertigo.stella.impl.workers.WorkersManagerImpl;
import io.vertigo.stella.master.MasterManager;
import io.vertigo.stella.plugins.work.redis.master.RedisMasterPlugin;
import io.vertigo.stella.plugins.work.redis.workers.RedisWorkersPlugin;
import io.vertigo.stella.plugins.work.rest.master.RestMasterPlugin;
import io.vertigo.stella.plugins.work.rest.master.RestMasterWebService;
import io.vertigo.stella.plugins.work.rest.workers.RestWorkersPlugin;
import io.vertigo.stella.workers.WorkersManager;

/**
 * Defines the 'stella' extension
 *
 * @author mlaroche
 */
public final class StellaFeatures extends Features<StellaFeatures> {

	/**
	 * Constructor.
	 */
	public StellaFeatures() {
		super("vertigo-stella");
	}

	@Feature("master")
	public StellaFeatures withMaster() {
		getModuleConfigBuilder().addComponent(MasterManager.class, MasterManagerImpl.class);
		return this;
	}

	@Feature("master.redis")
	public StellaFeatures withRedisMasterPlugin() {
		getModuleConfigBuilder().addPlugin(RedisMasterPlugin.class);
		return this;
	}

	@Feature("master.rest")
	public StellaFeatures withRestMasterPlugin(final Param... params) {
		getModuleConfigBuilder()
				.addPlugin(RestMasterPlugin.class, params)
				.addComponent(RestMasterWebService.class);
		return this;
	}

	@Feature("worker")
	public StellaFeatures withWorker(final Param... params) {
		getModuleConfigBuilder().addComponent(WorkersManager.class, WorkersManagerImpl.class, params);
		return this;
	}

	@Feature("worker.redis")
	public StellaFeatures withRedisWorkerPlugin() {
		getModuleConfigBuilder().addPlugin(RedisWorkersPlugin.class);
		return this;
	}

	@Feature("worker.rest")
	public StellaFeatures withRestWorkerPlugin(final Param... params) {
		getModuleConfigBuilder().addPlugin(RestWorkersPlugin.class, params);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	protected void buildFeatures() {
		// nothing by default (either master or worker or both)
	}

}
