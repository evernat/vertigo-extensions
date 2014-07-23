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
package io.vertigo.rest.rest;

import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.metamodel.DtField.FieldType;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.kernel.lang.MessageText;
import io.vertigo.rest.rest.validation.AbstractDtObjectValidator;
import io.vertigo.rest.rest.validation.DtObjectErrors;

public final class MandatoryPkValidator<O extends DtObject> extends AbstractDtObjectValidator<O> {

	/** {@inheritDoc} */
	@Override
	protected void checkMonoFieldConstraints(final O dtObject, final DtField dtField, final DtObjectErrors dtObjectErrors) {
		final String camelCaseFieldName = getCamelCaseFieldName(dtField);
		if (dtField.getType() == FieldType.PRIMARY_KEY && !dtObjectErrors.hasError(camelCaseFieldName)) {
			if (DtObjectUtil.getId(dtObject) == null) {
				dtObjectErrors.addError(camelCaseFieldName, new MessageText("Id is mandatory", null));
			}
		}
	}
}
