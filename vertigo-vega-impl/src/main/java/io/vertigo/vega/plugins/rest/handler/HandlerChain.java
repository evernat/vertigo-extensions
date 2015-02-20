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
package io.vertigo.vega.plugins.rest.handler;

import io.vertigo.lang.Assertion;
import io.vertigo.vega.impl.rest.RestHandlerPlugin;
import io.vertigo.vega.rest.exception.SessionException;
import io.vertigo.vega.rest.exception.VSecurityException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import spark.Request;
import spark.Response;

/**
 * Chain of handlers to handle a Request.
 * @author npiedeloup
 */
public final class HandlerChain {
	private final List<RestHandlerPlugin> handlers;
	private final int offset;

	/**
	 * Constructor.
	 * @param handlers Handlers
	 */
	public HandlerChain(final List<RestHandlerPlugin> handlers) {
		Assertion.checkNotNull(handlers);
		//-----
		this.handlers = Collections.unmodifiableList(new ArrayList<>(handlers));
		offset = 0;
	}

	/**
	 * private constructor for go forward in chain
	 */
	private HandlerChain(final List<RestHandlerPlugin> handlers, final int offset) {
		Assertion.checkState(offset < 50, "HandlerChain go through 50 handlers. Force halt : infinit loop suspected.");
		//-----
		this.handlers = handlers;
		this.offset = offset + 1; //new offset
	}

	/**
	 * Do handle of this route.
	 *
	 * @param request spark.Request
	 * @param response spark.Response
	 * @param routeContext Context of this route
	 * @return WebService result
	 * @throws VSecurityException Security exception
	 * @throws SessionException Session exception
	 */
	public Object handle(final Request request, final Response response, final RouteContext routeContext) throws VSecurityException, SessionException {
		int lookAhead = 0;
		while (offset + lookAhead < handlers.size()) {
			final RestHandlerPlugin nextHandler = handlers.get(offset + lookAhead);
			// >>> before doFilter " + nextHandler
			if (nextHandler.accept(routeContext.getEndPointDefinition())) {
				return nextHandler.handle(request, response, routeContext, new HandlerChain(handlers, offset + lookAhead));
			}
			//if current  doesn't apply for this EndPointDefinition we look ahead
			lookAhead++;
			// <<< after doFilter " + nextHandler
		}
		throw new IllegalStateException("Last RestHandlerPlugin haven't send a response body");
	}

}
