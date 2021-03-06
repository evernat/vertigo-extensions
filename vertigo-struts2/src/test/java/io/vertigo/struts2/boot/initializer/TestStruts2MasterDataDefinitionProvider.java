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
/**
 *
 */
package io.vertigo.struts2.boot.initializer;

import io.vertigo.dynamo.impl.store.datastore.AbstractMasterDataDefinitionProvider;
import io.vertigo.struts2.domain.movies.Movie;
import io.vertigo.struts2.domain.reference.Commune;
import io.vertigo.struts2.domain.reference.OuiNonChoice;
import io.vertigo.struts2.domain.users.Profil;
import io.vertigo.struts2.domain.users.SecurityRole;

/**
 * Init masterdata list.
 * @author jmforhan
 */
public class TestStruts2MasterDataDefinitionProvider extends AbstractMasterDataDefinitionProvider {

	@Override
	public void declareMasterDataLists() {
		registerDtMasterDatas(Profil.class);
		registerDtMasterDatas(SecurityRole.class);
		registerDtMasterDatas(Movie.class);

		registerDtMasterDatas(OuiNonChoice.class);
		//Liste de référence des communes-CP
		registerDtMasterDatas(Commune.class, false);
	}

}
