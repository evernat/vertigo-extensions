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
public final class OProcessExecution implements Entity {
	private static final long serialVersionUID = 1L;

	private Long preId;
	private java.util.Date beginTime;
	private java.util.Date endTime;
	private String engine;
	private Boolean checked;
	private java.util.Date checkingDate;
	private String checkingComment;

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_PRE_PRO",
			fkFieldName = "PRO_ID",
			primaryDtDefinitionName = "DT_O_PROCESS",
			primaryIsNavigable = true,
			primaryRole = "Process",
			primaryLabel = "Processus",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DT_O_PROCESS_EXECUTION",
			foreignIsNavigable = false,
			foreignRole = "ExecutionProcessus",
			foreignLabel = "ExecutionProcessus",
			foreignMultiplicity = "0..*")
	private final VAccessor<io.vertigo.orchestra.domain.definition.OProcess> proIdAccessor = new VAccessor<>(io.vertigo.orchestra.domain.definition.OProcess.class, "Process");

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_PRE_EST",
			fkFieldName = "EST_CD",
			primaryDtDefinitionName = "DT_O_EXECUTION_STATE",
			primaryIsNavigable = true,
			primaryRole = "ExecutionState",
			primaryLabel = "ExecutionState",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DT_O_PROCESS_EXECUTION",
			foreignIsNavigable = false,
			foreignRole = "ExecutionProcess",
			foreignLabel = "ExecutionProcessus",
			foreignMultiplicity = "0..*")
	private final VAccessor<io.vertigo.orchestra.domain.referential.OExecutionState> estCdAccessor = new VAccessor<>(io.vertigo.orchestra.domain.referential.OExecutionState.class, "ExecutionState");

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_PRE_USR",
			fkFieldName = "USR_ID",
			primaryDtDefinitionName = "DT_O_USER",
			primaryIsNavigable = true,
			primaryRole = "User",
			primaryLabel = "User",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DT_O_PROCESS_EXECUTION",
			foreignIsNavigable = false,
			foreignRole = "ExecutionProcess",
			foreignLabel = "ExecutionProcessus",
			foreignMultiplicity = "0..*")
	private final VAccessor<io.vertigo.orchestra.domain.referential.OUser> usrIdAccessor = new VAccessor<>(io.vertigo.orchestra.domain.referential.OUser.class, "User");

	/** {@inheritDoc} */
	@Override
	public UID<OProcessExecution> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id de l'execution d'un processus'.
	 * @return Long preId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_IDENTIFIANT", type = "ID", required = true, label = "Id de l'execution d'un processus")
	public Long getPreId() {
		return preId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id de l'execution d'un processus'.
	 * @param preId Long <b>Obligatoire</b>
	 */
	public void setPreId(final Long preId) {
		this.preId = preId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de début'.
	 * @return Date beginTime <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_TIMESTAMP", required = true, label = "Date de début")
	public java.util.Date getBeginTime() {
		return beginTime;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de début'.
	 * @param beginTime Date <b>Obligatoire</b>
	 */
	public void setBeginTime(final java.util.Date beginTime) {
		this.beginTime = beginTime;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de fin'.
	 * @return Date endTime
	 */
	@Field(domain = "DO_O_TIMESTAMP", label = "Date de fin")
	public java.util.Date getEndTime() {
		return endTime;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de fin'.
	 * @param endTime Date
	 */
	public void setEndTime(final java.util.Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Implémentation effective de l'execution'.
	 * @return String engine
	 */
	@Field(domain = "DO_O_CLASSE", label = "Implémentation effective de l'execution")
	public String getEngine() {
		return engine;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Implémentation effective de l'execution'.
	 * @param engine String
	 */
	public void setEngine(final String engine) {
		this.engine = engine;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Pris en charge'.
	 * @return Boolean checked
	 */
	@Field(domain = "DO_O_BOOLEEN", label = "Pris en charge")
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Pris en charge'.
	 * @param checked Boolean
	 */
	public void setChecked(final Boolean checked) {
		this.checked = checked;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de prise en charge'.
	 * @return Date checkingDate
	 */
	@Field(domain = "DO_O_TIMESTAMP", label = "Date de prise en charge")
	public java.util.Date getCheckingDate() {
		return checkingDate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de prise en charge'.
	 * @param checkingDate Date
	 */
	public void setCheckingDate(final java.util.Date checkingDate) {
		this.checkingDate = checkingDate;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Commentaire'.
	 * @return String checkingComment
	 */
	@Field(domain = "DO_O_TEXT", label = "Commentaire")
	public String getCheckingComment() {
		return checkingComment;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Commentaire'.
	 * @param checkingComment String
	 */
	public void setCheckingComment(final String checkingComment) {
		this.checkingComment = checkingComment;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Processus'.
	 * @return Long proId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_IDENTIFIANT", type = "FOREIGN_KEY", required = true, label = "Processus")
	public Long getProId() {
		return (Long) proIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Processus'.
	 * @param proId Long <b>Obligatoire</b>
	 */
	public void setProId(final Long proId) {
		proIdAccessor.setId(proId);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'ExecutionState'.
	 * @return String estCd
	 */
	@Field(domain = "DO_O_CODE_IDENTIFIANT", type = "FOREIGN_KEY", label = "ExecutionState")
	public String getEstCd() {
		return (String) estCdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'ExecutionState'.
	 * @param estCd String
	 */
	public void setEstCd(final String estCd) {
		estCdAccessor.setId(estCd);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'User'.
	 * @return Long usrId
	 */
	@Field(domain = "DO_O_IDENTIFIANT", type = "FOREIGN_KEY", label = "User")
	public Long getUsrId() {
		return (Long) usrIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'User'.
	 * @param usrId Long
	 */
	public void setUsrId(final Long usrId) {
		usrIdAccessor.setId(usrId);
	}

 	/**
	 * Association : ExecutionState.
	 * @return l'accesseur vers la propriété 'ExecutionState'
	 */
	public VAccessor<io.vertigo.orchestra.domain.referential.OExecutionState> executionState() {
		return estCdAccessor;
	}

 	/**
	 * Association : Processus.
	 * @return l'accesseur vers la propriété 'Processus'
	 */
	public VAccessor<io.vertigo.orchestra.domain.definition.OProcess> process() {
		return proIdAccessor;
	}

 	/**
	 * Association : User.
	 * @return l'accesseur vers la propriété 'User'
	 */
	public VAccessor<io.vertigo.orchestra.domain.referential.OUser> user() {
		return usrIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
