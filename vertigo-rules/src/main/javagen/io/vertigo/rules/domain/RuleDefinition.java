package io.vertigo.rules.domain;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.ListVAccessor;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class RuleDefinition implements Entity {
	private static final long serialVersionUID = 1L;

	private Long id;
	private java.util.Date creationDate;
	private Long itemId;
	private String label;

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_RUD_COD",
			fkFieldName = "RUD_ID",
			primaryDtDefinitionName = "DT_RULE_DEFINITION",
			primaryIsNavigable = false,
			primaryRole = "RuleDefinition",
			primaryLabel = "RuleDefinition",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DT_RULE_CONDITION_DEFINITION",
			foreignIsNavigable = true,
			foreignRole = "RuleConditionDefinition",
			foreignLabel = "RuleConditionDefinition",
			foreignMultiplicity = "0..*")
	private final ListVAccessor<io.vertigo.rules.domain.RuleConditionDefinition> ruleConditionDefinitionAccessor = new ListVAccessor<>(this, "A_RUD_COD", "RuleConditionDefinition");

	/** {@inheritDoc} */
	@Override
	public UID<RuleDefinition> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'id'.
	 * @return Long id <b>Obligatoire</b>
	 */
	@Field(domain = "DO_RULES_ID", type = "ID", required = true, label = "id")
	public Long getId() {
		return id;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'id'.
	 * @param id Long <b>Obligatoire</b>
	 */
	public void setId(final Long id) {
		this.id = id;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'creationDate'.
	 * @return Date creationDate
	 */
	@Field(domain = "DO_RULES_DATE", label = "creationDate")
	public java.util.Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'creationDate'.
	 * @param creationDate Date
	 */
	public void setCreationDate(final java.util.Date creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'itemId'.
	 * @return Long itemId
	 */
	@Field(domain = "DO_RULES_WEAK_ID", label = "itemId")
	public Long getItemId() {
		return itemId;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'itemId'.
	 * @param itemId Long
	 */
	public void setItemId(final Long itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Label'.
	 * @return String label
	 */
	@Field(domain = "DO_RULES_LABEL", label = "Label")
	public String getLabel() {
		return label;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Label'.
	 * @param label String
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * Association : RuleConditionDefinition.
	 * @return l'accesseur vers la propriété 'RuleConditionDefinition'
	 */
	public ListVAccessor<io.vertigo.rules.domain.RuleConditionDefinition> ruleConditionDefinition() {
		return ruleConditionDefinitionAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
