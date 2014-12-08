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
package io.vertigo.core.config;

import io.vertigo.core.aop.Aspect;
import io.vertigo.lang.Assertion;

/**
 * Définition d'un aspect.
 * Un aspect est la réunion
 *  - d'un point d'interception défini par une annotation
 *  - d'un intercepteur (advice) défini par un composant
 *
 * @author pchretien
 */
public final class AspectConfig {
	private final Class<? extends Aspect> implClass;

	/**
	 * Constructeur.
	 */
	AspectConfig(final Class<? extends Aspect> implClass) {
		Assertion.checkNotNull(implClass);
		//---------------------------------------------------------------------
		this.implClass = implClass;
	}

	/**
	 * @return Classe d'implémentation du composant d'interception
	 */
	public Class<? extends Aspect> getAspectImplClass() {
		return implClass;
	}
}
