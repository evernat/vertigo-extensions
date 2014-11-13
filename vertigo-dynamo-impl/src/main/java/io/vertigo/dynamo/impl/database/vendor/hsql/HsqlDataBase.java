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
package io.vertigo.dynamo.impl.database.vendor.hsql;

import io.vertigo.dynamo.database.vendor.SqlDataBase;
import io.vertigo.dynamo.database.vendor.SqlExceptionHandler;
import io.vertigo.dynamo.database.vendor.SqlMapping;
import io.vertigo.dynamo.impl.database.vendor.core.SqlMappingImpl;

/**
 * Gestion de la base de données HSQL.
 *
 * @author pchretien
 */
public final class HsqlDataBase implements SqlDataBase {
	private final SqlExceptionHandler sqlExceptionHandler = new HsqlExceptionHandler();
	private final SqlMapping sqlMapping = new SqlMappingImpl();

	/** {@inheritDoc} */
	@Override
	public SqlExceptionHandler getSqlExceptionHandler() {
		return sqlExceptionHandler;
	}

	/** {@inheritDoc} */
	@Override
	public SqlMapping getSqlMapping() {
		return sqlMapping;
	}
}
