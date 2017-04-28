/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2017, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.persona.security.model;

import io.vertigo.dynamo.domain.model.KeyConcept;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * Dossier.
 */
public final class Dossier implements KeyConcept {
	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Long dosId;

	private Long regId;
	private Long depId;
	private Long comId;

	private Long typId;
	private String titre;
	private Double montant;
	private Long utiIdOwner;

	private String etaCd;

	/** {@inheritDoc} */
	@Override
	public URI<Dossier> getURI() {
		return DtObjectUtil.createURI(this);
	}

	@Field(domain = "DO_ID", type = "ID", required = true, label = "Id")
	public final Long getDosId() {
		return dosId;
	}

	public final void setDosId(final Long dosId) {
		this.dosId = dosId;
	}

	@Field(domain = "DO_ID", label = "Region")
	public final Long getRegId() {
		return regId;
	}

	public final void setRegId(final Long regId) {
		this.regId = regId;
	}

	@Field(domain = "DO_ID", label = "Département")
	public final Long getDepId() {
		return depId;
	}

	public final void setDepId(final Long depId) {
		this.depId = depId;
	}

	@Field(domain = "DO_ID", label = "Commune")
	public final Long getComId() {
		return comId;
	}

	public final void setComId(final Long comId) {
		this.comId = comId;
	}

	@Field(domain = "DO_ID", required = true, label = "Type dossier")
	public final Long getTypId() {
		return typId;
	}

	public final void setTypId(final Long typId) {
		this.typId = typId;
	}

	@Field(domain = "DO_LIBELLE", required = true, label = "Titre")
	public final String getTitre() {
		return titre;
	}

	public final void setTitre(final String titre) {
		this.titre = titre;
	}

	@Field(domain = "DO_MONTANT", required = true, label = "Montant")
	public final Double getMontant() {
		return montant;
	}

	public final void setMontant(final Double montant) {
		this.montant = montant;
	}

	@Field(domain = "DO_ID", required = true, label = "Créateur")
	public final Long getUtiIdOwner() {
		return utiIdOwner;
	}

	public final void setUtiIdOwner(final Long utiIdOwner) {
		this.utiIdOwner = utiIdOwner;
	}

	@Field(domain = "DO_CODE", required = true, label = "Etat dossier")
	public final String getEtaCd() {
		return etaCd;
	}

	public final void setEtaCd(final String etaCd) {
		this.etaCd = etaCd;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
