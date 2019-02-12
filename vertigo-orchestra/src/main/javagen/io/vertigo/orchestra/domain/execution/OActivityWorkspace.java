package io.vertigo.orchestra.domain.execution;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.model.VAccessor;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.dynamo.domain.stereotype.DataSpace("orchestra")
public final class OActivityWorkspace implements Entity {
	private static final long serialVersionUID = 1L;

	private Long acwId;
	private Boolean isIn;
	private String workspace;

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_TKW_TKE",
			fkFieldName = "ACE_ID",
			primaryDtDefinitionName = "DT_O_ACTIVITY_EXECUTION",
			primaryIsNavigable = true,
			primaryRole = "ActivityExecution",
			primaryLabel = "ActivityExecution",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DT_O_ACTIVITY_WORKSPACE",
			foreignIsNavigable = false,
			foreignRole = "ActivityWorkspace",
			foreignLabel = "ActivityWorkspace",
			foreignMultiplicity = "0..*")
	private final VAccessor<io.vertigo.orchestra.domain.execution.OActivityExecution> aceIdAccessor = new VAccessor<>(io.vertigo.orchestra.domain.execution.OActivityExecution.class, "ActivityExecution");

	/** {@inheritDoc} */
	@Override
	public UID<OActivityWorkspace> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id de l'execution d'un processus'.
	 * @return Long acwId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_IDENTIFIANT", type = "ID", required = true, label = "Id de l'execution d'un processus")
	public Long getAcwId() {
		return acwId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id de l'execution d'un processus'.
	 * @param acwId Long <b>Obligatoire</b>
	 */
	public void setAcwId(final Long acwId) {
		this.acwId = acwId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Workspace in/out'.
	 * @return Boolean isIn <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_BOOLEEN", required = true, label = "Workspace in/out")
	public Boolean getIsIn() {
		return isIn;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Workspace in/out'.
	 * @param isIn Boolean <b>Obligatoire</b>
	 */
	public void setIsIn(final Boolean isIn) {
		this.isIn = isIn;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Contenu du workspace'.
	 * @return String workspace
	 */
	@Field(domain = "DO_O_JSON_TEXT", label = "Contenu du workspace")
	public String getWorkspace() {
		return workspace;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Contenu du workspace'.
	 * @param workspace String
	 */
	public void setWorkspace(final String workspace) {
		this.workspace = workspace;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'ActivityExecution'.
	 * @return Long aceId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_IDENTIFIANT", type = "FOREIGN_KEY", required = true, label = "ActivityExecution")
	public Long getAceId() {
		return (Long) aceIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'ActivityExecution'.
	 * @param aceId Long <b>Obligatoire</b>
	 */
	public void setAceId(final Long aceId) {
		aceIdAccessor.setId(aceId);
	}

 	/**
	 * Association : ActivityExecution.
	 * @return l'accesseur vers la propriété 'ActivityExecution'
	 */
	public VAccessor<io.vertigo.orchestra.domain.execution.OActivityExecution> activityExecution() {
		return aceIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
