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
package io.vertigo.persona.security;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.vertigo.core.definition.DefinitionReference;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.lang.Assertion;
import io.vertigo.persona.security.metamodel.Permission;
import io.vertigo.persona.security.metamodel.PermissionName;

/**
 * Session d'un utilisateur.
 * Un utilisateur
 * <ul>
 * <li>est authentifie ou non,</li>
 * <li>possede une liste de permissions (prealablement enregistres dans la PermissionRegistry),</li>
 * <li>possède une liste d'attributs serialisables</li>.
 * </ul>
 *
 * @author alauthier, pchretien
 */
public abstract class UserSession implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 467617818948129397L;

	/**
	 * Cle de la session (utilise comme cle unique de connexion).
	 */
	private final UUID sessionUUID = createUUID();

	/**
	 * Set des permissions global autorisees pour la session utilisateur.
	 */
	private final Map<String, DefinitionReference<Permission>> permissionRefs = new HashMap<>();

	/**
	 * Set des permissions autorisees par entity pour la session utilisateur.
	 */
	private final Map<DefinitionReference<DtDefinition>, Set<DefinitionReference<Permission>>> permissionMapRefs = new HashMap<>();

	/**
	 * Attributs supplémentaires associées à la session.
	 */
	private final Map<String, Serializable> attributes = new HashMap<>();

	/**
	 * Indique si l'utilisateur est authentifie.
	 * Par defaut l'utilisateur n'est pas authentifie.
	 */
	private boolean authenticated;

	private static UUID createUUID() {
		//On utilise le mecanisme de creation standard.
		return UUID.randomUUID();
	}

	//===========================================================================
	//=======================GESTION DES ROLES===================================
	//===========================================================================
	/**
	 * Ajoute une permission pour l'utilisateur courant.
	 * La permission doit avoir ete prealablement enregistree.
	 *
	 * @param permission Permission à ajouter.
	 * @return This UserSession
	 */
	public final UserSession addPermission(final Permission permission) {
		Assertion.checkNotNull(permission);
		//-----
		permissionRefs.put(permission.getName(), new DefinitionReference<>(permission));
		if (permission.getEntityDefinition().isPresent()) {
			permissionMapRefs.computeIfAbsent(new DefinitionReference<>(permission.getEntityDefinition().get()), key -> new HashSet<>())
					.add(new DefinitionReference<>(permission));
		}
		return this;
	}

	/**
	 * Retourne la liste des permissions de securite d'une entity pour l'utilisateur.
	 * @param entityDefinition Entity definition
	 * @return Set des permissions.
	 */
	public final Set<Permission> getEntityPermissions(final DtDefinition entityDefinition) {
		final Set<DefinitionReference<Permission>> entityPermissionRef = permissionMapRefs.get(new DefinitionReference<>(entityDefinition));
		final Set<Permission> permissionSet = new HashSet<>();
		if (entityPermissionRef != null) {
			for (final DefinitionReference<Permission> permissionReference : entityPermissionRef) {
				permissionSet.add(permissionReference.get());
			}
		}
		return Collections.unmodifiableSet(permissionSet);
	}

	/**
	 * @param permissionName Permission
	 * @return Vrai si la permission est presente
	 */
	public final boolean hasPermission(final PermissionName permissionName) {
		Assertion.checkNotNull(permissionName);
		//-----
		return permissionRefs.containsKey(permissionName.name());
	}

	/**
	 * Retrait de toutes les permissions possedes par l'utilisateur.
	 * Attention, cela signifie qu'il n'a plus aucun droit.
	 */
	public final void clearPermissions() {
		permissionRefs.clear();
		permissionMapRefs.clear();
	}

	//===========================================================================
	//=======================GESTION DES AUTHENTIFICATIONS=======================
	//===========================================================================

	/**
	 * @return UUID Indentifiant unique de cette connexion.
	 */
	public final UUID getSessionUUID() {
		return sessionUUID;
	}

	/**
	 * Indique si l'utilisateur est authentifie.
	 * L'authentification est actée par l'appel de la méthode <code>authenticate()</code>
	 *
	 * @return boolean Si l'utilisateur s'est authentifié.
	 */
	public final boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * Méthode permettant d'indiquer que l'utilisateur est authentifié.
	 */
	public final void authenticate() {
		authenticated = true;
	}

	/**
	 * Ajout d'attribut supplémentaire.
	 * @param key Key
	 * @param value Value
	 */
	public final void putAttribute(final String key, final Serializable value) {
		attributes.put(key, value);
	}

	/**
	 * Get d'attribut supplémentaire.
	 * @param key Key
	 * @param <O> Value type
	 * @return attribute value.
	 */
	public final <O extends Serializable> O getAttribute(final String key) {
		return (O) attributes.get(key);
	}

	/**
	 * Gestion multilingue.
	 * Local associée à l'utilisateur.
	 * @return Locale associée à l'utilisateur.
	 */
	public abstract Locale getLocale();

	/**
	 * Gestion de la sécurité.
	 * @return Liste des clés de sécurité et leur valeur.
	 */
	public Map<String, Comparable[]> getSecurityKeys() {
		return Collections.emptyMap();
	}
}
