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
package io.vertigo.social.webservices.comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import io.vertigo.account.account.Account;
import io.vertigo.account.authentication.AuthenticationManager;
import io.vertigo.account.authorization.VSecurityException;
import io.vertigo.app.Home;
import io.vertigo.core.locale.MessageText;
import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.model.KeyConcept;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.lang.Assertion;
import io.vertigo.social.services.comment.Comment;
import io.vertigo.social.services.comment.CommentServices;
import io.vertigo.util.MapBuilder;
import io.vertigo.vega.webservice.WebServices;
import io.vertigo.vega.webservice.stereotype.AnonymousAccessAllowed;
import io.vertigo.vega.webservice.stereotype.ExcludedFields;
import io.vertigo.vega.webservice.stereotype.GET;
import io.vertigo.vega.webservice.stereotype.POST;
import io.vertigo.vega.webservice.stereotype.PUT;
import io.vertigo.vega.webservice.stereotype.PathParam;
import io.vertigo.vega.webservice.stereotype.PathPrefix;
import io.vertigo.vega.webservice.stereotype.QueryParam;

/**
 * Webservice for Notification extension.
 *
 * @author npiedeloup
 */
@PathPrefix("/x/comment")
public final class CommentWebServices implements WebServices {

	private static final String API_VERSION = "0.1.0";
	private static final String IMPL_VERSION = "0.9.2";

	@Inject
	private CommentServices commentServices;

	@Inject
	private AuthenticationManager authenticationManager;

	/**
	 * Gets comments for keyConcept.
	 * @param keyConcept KeyConcept type
	 * @param id KeyConcept id
	 * @return comments for keyConcept
	 */
	@GET("/api/comments")
	public List<Comment> getComments(@QueryParam("concept") final String keyConcept, @QueryParam("id") final String id) {
		final UID<KeyConcept> keyConceptURI = readKeyConceptURI(keyConcept, id);
		return commentServices.getComments(keyConceptURI);
	}

	/**
	 * Publishes a new comment.
	 * @param comment Comment msg
	 * @param keyConcept KeyConcept type
	 * @param id KeyConcept id
	 */
	@POST("/api/comments")
	public Comment publishComment(@ExcludedFields("uuid") final Comment comment, @QueryParam("concept") final String keyConcept, @QueryParam("id") final String id) {
		final UID<KeyConcept> keyConceptURI = readKeyConceptURI(keyConcept, id);
		commentServices.publish(getLoggedAccountURI(), comment, keyConceptURI);
		return comment;
	}

	/**
	 * Updates a comment.
	 * @param uuid Comment uuid
	 * @param comment Comment msg
	 */
	@PUT("/api/comments/{uuid}")
	public Comment updateComment(@PathParam("uuid") final String uuid, final Comment comment) {
		Assertion.checkNotNull(uuid);
		Assertion.checkNotNull(comment);
		Assertion.checkArgument(uuid.equals(comment.getUuid().toString()), "Comment uuid ({0}) must match WebService route ({1})", comment.getUuid(), uuid);
		//-----
		commentServices.update(getLoggedAccountURI(), comment);
		return comment;
	}

	//-----
	/**
	 * Returns status (code 200 or 500)
	 * @return "OK" or error message
	 */
	@GET("/status")
	@AnonymousAccessAllowed
	public String getStatus() {
		return "OK";
	}

	/**
	 * Returns  stats.
	 * @return "OK" or error message
	 */
	@GET("/stats")
	@AnonymousAccessAllowed
	public Map<String, Object> getStats() {
		final Map<String, Object> stats = new HashMap<>();
		final Map<String, Object> sizeStats = new HashMap<>();
		sizeStats.put("comments", "not yet");
		stats.put("size", sizeStats);
		return stats;
	}

	/**
	 * Returns  config.
	 * @return Config object
	 */
	@GET("/config")
	@AnonymousAccessAllowed
	public Map<String, Object> getConfig() {
		return new MapBuilder<String, Object>()
				.put("api-version", API_VERSION)
				.put("impl-version", IMPL_VERSION)
				.build();
	}

	/**
	 * Returns  help.
	 * @return Help object
	 */
	@GET("/help")
	@AnonymousAccessAllowed
	public String getHelp() {
		return "##Comment extension"
				+ "\n This extension manage the comment center.";
	}

	private static UID<KeyConcept> readKeyConceptURI(final String keyConcept, @QueryParam("id") final String id) {
		final DtDefinition dtDefinition = Home.getApp().getDefinitionSpace().resolve("Dt" + keyConcept, DtDefinition.class);
		final Object keyConceptId = stringToId(id, dtDefinition);
		return UID.of(dtDefinition, keyConceptId);
	}

	private static Object stringToId(final String id, final DtDefinition dtDefinition) {
		final Optional<DtField> idFieldOption = dtDefinition.getIdField();
		Assertion.checkArgument(idFieldOption.isPresent(), "KeyConcept {0} must have an id field, in order to support Comment extension", dtDefinition.getLocalName());

		final DataType dataType = idFieldOption.get().getDomain().getDataType();
		if (dataType == DataType.String) {
			return id;
		} else if (dataType == DataType.Integer) {
			return Integer.valueOf(id);
		} else if (dataType == DataType.Long) {
			return Long.valueOf(id);
		}
		throw new IllegalArgumentException("the id of the keyConcept " + dtDefinition.getLocalName() + " must be String, Long or Integer");
	}

	private UID<Account> getLoggedAccountURI() {
		return authenticationManager.getLoggedAccount()
				.orElseThrow(() -> new VSecurityException(MessageText.of("No account logged in")))
				.getUID();
	}

}
