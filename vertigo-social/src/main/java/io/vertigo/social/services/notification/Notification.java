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
package io.vertigo.social.services.notification;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import io.vertigo.lang.Assertion;

/**
 * @author pchretien, npiedeloup, btounkara
 */
public final class Notification {
	private final UUID uuid;
	private final String sender;
	private final String type;
	private final String title;
	private final String content;
	private final int ttlInSeconds;
	private final String targetUrl;
	private final Instant creationDate;
	private final Optional<String> userContent;

	/**
	 * Constructor.
	 * @param uuid Uuid
	 * @param sender Sender name
	 * @param type Type
	 * @param title Title
	 * @param content Content
	 * @param ttlInSeconds TimeToLive in seconds
	 * @param creationDate Create date
	 * @param targetUrl Target URL of this notification
	 * @param userContent Reader's specific content of this notification (can't be empty)
	 */
	Notification(final UUID uuid, final String sender, final String type, final String title, final String content,
			final int ttlInSeconds, final Instant creationDate, final String targetUrl, final Optional<String> userContent) {
		Assertion.checkNotNull(uuid);
		Assertion.checkArgNotEmpty(sender);
		Assertion.checkArgNotEmpty(type);
		Assertion.checkArgNotEmpty(title);
		Assertion.checkArgNotEmpty(content);
		Assertion.checkArgument(ttlInSeconds == -1 || ttlInSeconds > 0, "ttl must be positive or undefined (-1).");
		Assertion.checkArgNotEmpty(targetUrl);
		Assertion.checkNotNull(creationDate);
		Assertion.checkNotNull(userContent);
		Assertion.when(userContent.isPresent()).check(() -> userContent.get().length() > 0, "userContent can't be empty if set");
		//-----
		this.uuid = uuid;
		this.sender = sender;
		this.type = type;
		this.title = title;
		this.content = content;
		this.ttlInSeconds = ttlInSeconds;
		this.creationDate = creationDate;
		this.targetUrl = targetUrl;
		this.userContent = userContent;
	}

	/**
	 * Static method factory for NotificationBuilder
	 * @return NotificationBuilder
	 */
	public static NotificationBuilder builder() {
		return new NotificationBuilder();
	}

	/**
	 * Static method factory for NotificationBuilder
	 * @param uuid Notification uuid
	 * @return NotificationBuilder
	 */
	public static NotificationBuilder builder(final UUID uuid) {
		return new NotificationBuilder(uuid);
	}

	/**
	 * @return Uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @return Sender's name
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @return Notification's type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Notification's type
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return Notification's content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return Notification's TTL in seconds
	 */
	public int getTTLInSeconds() {
		return ttlInSeconds;
	}

	/**
	 * @return Creation date
	 */
	public Instant getCreationDate() {
		return creationDate;
	}

	/**
	 * @return Notification's target url
	 */
	public String getTargetUrl() {
		return targetUrl;
	}

	/**
	 * @return Specific content linked to reader
	 */
	public Optional<String> getUserContent() {
		return userContent;
	}

}
