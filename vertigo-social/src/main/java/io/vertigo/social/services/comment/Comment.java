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
package io.vertigo.social.services.comment;

import java.time.Instant;
import java.util.UUID;

import io.vertigo.account.account.Account;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.lang.Assertion;

/**
 * @author pchretien
 */
public final class Comment {
	private final UUID uuid;
	private final UID<Account> author;
	private final String authorDisplayName;
	private final String msg;
	private final Instant creationDate;
	private final Instant lastModified;

	Comment(final UUID uuid, final UID<Account> author, final String authorDisplayName, final String msg, final Instant creationDate, final Instant lastModified) {
		Assertion.checkNotNull(uuid);
		Assertion.checkNotNull(author);
		Assertion.checkArgNotEmpty(authorDisplayName);
		Assertion.checkArgNotEmpty(msg);
		Assertion.checkNotNull(creationDate);
		//lastModified is nullable
		//-----
		this.uuid = uuid;
		this.author = author;
		this.authorDisplayName = authorDisplayName;
		this.msg = msg;
		this.creationDate = creationDate;
		this.lastModified = lastModified;
	}

	/**
	 * Static method factory for CommentBuilder
	 * @return CommentBuilder
	 */
	public static CommentBuilder builder() {
		return new CommentBuilder();
	}

	public UUID getUuid() {
		return uuid;
	}

	public UID<Account> getAuthor() {
		return author;
	}

	public String getAuthorDisplayName() {
		return authorDisplayName;
	}

	public String getMsg() {
		return msg;
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public Instant getLastModified() {
		return lastModified;
	}

}
