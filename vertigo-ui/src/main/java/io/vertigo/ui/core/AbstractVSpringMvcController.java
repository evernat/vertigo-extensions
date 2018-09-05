/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.ui.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.commons.transaction.VTransactionWritable;
import io.vertigo.core.param.ParamManager;
import io.vertigo.dynamo.kvstore.KVStoreManager;
import io.vertigo.lang.Assertion;
import io.vertigo.ui.exception.ExpiredContextException;

/**
 * Super class des Actions SpringMvc.
 *
 * @author npiedeloup, mlaroche
 */
public abstract class AbstractVSpringMvcController {

	/** Clé de la collection des contexts dans le KVStoreManager. */
	public static final String CONTEXT_COLLECTION_NAME = "VActionContext";

	/** Clé de context du UiUtil. */
	public static final String UTIL_CONTEXT_KEY = "util";
	/** Clé de context du mode. */
	public static final String MODE_CONTEXT_KEY = "mode";
	//TODO voir pour déléguer cette gestion des modes
	/** Clé de context du mode Edit. */
	public static final String MODE_EDIT_CONTEXT_KEY = "modeEdit";
	/** Clé de context du mode ReadOnly. */
	public static final String MODE_READ_ONLY_CONTEXT_KEY = "modeReadOnly";
	/** Clé de context du mode Create. */
	public static final String MODE_CREATE_CONTEXT_KEY = "modeCreate";
	/** Préfix des clés des paramètres passés par l'url. */
	public static final String URL_PARAM_PREFIX = "params.";

	private static final String NONE = "refresh";

	/**
	 * Indique que l'initialisation du context par un parametre de l'url est autorisé.
	 */
	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface AcceptCtxQueryParam {
		//rien
	}

	@Inject
	private KVStoreManager kvStoreManager;
	@Inject
	private VTransactionManager transactionManager;
	@Inject
	private ParamManager paramManager;

	public void prepareContext(final HttpServletRequest request) throws ExpiredContextException {
		final RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
		ViewContext viewContext = null;
		final String ctxId = request.getParameter(ViewContext.CTX);
		if ("POST".equals(request.getMethod()) || ctxId != null && acceptCtxQueryParam()) {
			if (ctxId == null) {
				contextMiss(null);
			} else {
				try (VTransactionWritable transactionWritable = transactionManager.createCurrentTransaction()) {
					viewContext = kvStoreManager.find(CONTEXT_COLLECTION_NAME, ctxId, ViewContext.class).get();
					transactionWritable.commit();
				}

				if (viewContext == null) {
					contextMiss(ctxId);
				}
				viewContext.makeModifiable();
			}
			attributes.setAttribute("viewContext", viewContext, RequestAttributes.SCOPE_REQUEST);
			attributes.setAttribute("uiMessageStack", new SpringMvcUiMessageStack(viewContext), RequestAttributes.SCOPE_REQUEST);
		} else {
			viewContext = new ViewContext();
			attributes.setAttribute("viewContext", viewContext, RequestAttributes.SCOPE_REQUEST);
			attributes.setAttribute("uiMessageStack", new SpringMvcUiMessageStack(viewContext), RequestAttributes.SCOPE_REQUEST);
			initContextUrlParameters(request, viewContext);
			//TODO vérifier que l'action demandée n'attendait pas de context : il va etre recrée vide ce qui n'est pas bon dans certains cas.
			preInitContext(viewContext);
			Assertion.checkState(viewContext.containsKey(UTIL_CONTEXT_KEY), "Pour surcharger preInitContext vous devez rappeler les parents super.preInitContext(). Action: {0}",
					getClass().getSimpleName());
			//initContext();
		}

	}

	@ModelAttribute
	public void storeContext(final Model model) {
		//model.addAllAttributes(getModel());
		model.addAttribute("model", getViewContext());
		// here we can retrieve anything and put it into the model or in our context
		// we can also use argument resolvers to retrieve attributes in our context for convenience (a DtObject or an UiObject can be retrieved as parameters
		// easily from our vContext since we have access to the modelandviewContainer in a parameterResolver...)

	}

	@ModelAttribute
	public void mapRequestParams(@ModelAttribute("model") final ViewContext kActionContext, final Model model) {
		// just use springMVC value mapper
		model.addAttribute("viewContextLoaded", Boolean.TRUE);

	}

	private boolean acceptCtxQueryParam() {
		return this.getClass().isAnnotationPresent(AcceptCtxQueryParam.class);
	}

	/**
	 * Appeler lorsque que le context est manquant.
	 * Par défaut lance une ExpiredContextException.
	 * Mais une action spécifique pourrait reconstruire le context si c'est pertinent.
	 * @param ctxId Id du context manquant (seule info disponible)
	 * @throws ExpiredContextException Context expiré (comportement standard)
	 */
	protected void contextMiss(final String ctxId) throws ExpiredContextException {
		throw new ExpiredContextException("Context ctxId:'" + ctxId + "' manquant");
	}

	/**
	//	 * Initialisation du context.
	//	 * Pour accepter initContext avec des paramètres de la request, il est possible de le faire avec ce code :
	//	 * <code>
	//	 * final RequestContainerWrapper container = new RequestContainerWrapper(ServletActionContext.getRequest());
	//	 * MethodUtil.invoke(this, "initContext", container);
	//	 * </code>
	//	 */
	//	protected abstract void initContext();

	/**
	 * Preinitialisation du context, pour ajouter les composants standard.
	 * Si surcharger doit rappeler le super.preInitContext();
	 */
	protected void preInitContext(final ViewContext viewContext) {
		viewContext.put("appVersion", paramManager.getParam("app.version").getValueAsString());
		viewContext.put(UTIL_CONTEXT_KEY, new UiUtil());
		toModeReadOnly();
	}

	/**
	 * Initialisation du context pour ajouter les paramètres passés par l'url.
	 * Les paramètres sont préfixés par "param."
	 */
	private static void initContextUrlParameters(final HttpServletRequest request, final ViewContext viewContext) {
		String name;
		for (final Enumeration<String> names = request.getParameterNames(); names.hasMoreElements();) {
			name = names.nextElement();
			viewContext.put(URL_PARAM_PREFIX + name, request.getParameterValues(name));
		}
	}

	/**
	 * Conserve et fige le context.
	 * Utilisé par le KActionContextStoreInterceptor.
	 */
	public final void storeContext() {
		final ViewContext viewContext = getViewContext();
		viewContext.makeUnmodifiable();
		try (VTransactionWritable transactionWritable = transactionManager.createCurrentTransaction()) {
			//Suite à SpringMvc 2.5 : les fichiers sont des UploadedFile non sérializable.
			//On vérifie qu'ils ont été consommés
			//			for (final Entry<String, Serializable> contextEntry : context.entrySet()) {
			//				if (contextEntry.getValue() instanceof UploadedFile[]) { //plus globalement !Serializable, mais on ne peut plus adapter le message
			//					throw new VSystemException("Le fichier '{0}' a été envoyé mais pas consommé par l'action", contextEntry.getKey());
			//					//sinon un warn, et on le retire du context ?
			//				}
			//			}
			kvStoreManager.put(CONTEXT_COLLECTION_NAME, viewContext.getId(), viewContext);
			transactionWritable.commit();
		}
	}

	/** {@inheritDoc} */

	public final void validate() {
		//rien
	}

	/** {@inheritDoc} */
	private final static ViewContext getViewContext() {
		final RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
		final ViewContext viewContext = (ViewContext) attributes.getAttribute("viewContext", RequestAttributes.SCOPE_REQUEST);
		Assertion.checkNotNull(viewContext);
		//---
		return viewContext;
	}

	/**
	 * Passe en mode edition.
	 */
	protected final static void toModeEdit() {
		//TODO voir pour déléguer cette gestion des modes
		final ViewContext viewContext = getViewContext();
		viewContext.put(MODE_CONTEXT_KEY, FormMode.edit);
		viewContext.put(MODE_READ_ONLY_CONTEXT_KEY, false);
		viewContext.put(MODE_EDIT_CONTEXT_KEY, true);
		viewContext.put(MODE_CREATE_CONTEXT_KEY, false);
	}

	/**
	 * Passe en mode creation.
	 */
	protected final static void toModeCreate() {
		//TODO voir pour déléguer cette gestion des modes
		final ViewContext viewContext = getViewContext();
		viewContext.put(MODE_CONTEXT_KEY, FormMode.create);
		viewContext.put(MODE_READ_ONLY_CONTEXT_KEY, false);
		viewContext.put(MODE_EDIT_CONTEXT_KEY, false);
		viewContext.put(MODE_CREATE_CONTEXT_KEY, true);
	}

	/**
	 * Passe en mode readonly.
	 */
	protected final static void toModeReadOnly() {
		//TODO voir pour déléguer cette gestion des modes
		final ViewContext viewContext = getViewContext();
		viewContext.put(MODE_CONTEXT_KEY, FormMode.readOnly);
		viewContext.put(MODE_READ_ONLY_CONTEXT_KEY, true);
		viewContext.put(MODE_EDIT_CONTEXT_KEY, false);
		viewContext.put(MODE_CREATE_CONTEXT_KEY, false);
	}

	/**
	 * @return Si on est en mode edition
	 */
	protected final static boolean isModeEdit() {
		final ViewContext viewContext = getViewContext();
		return FormMode.edit.equals(viewContext.get(MODE_CONTEXT_KEY));
	}

	/**
	 * @return Si on est en mode readOnly
	 */
	protected final static boolean isModeRead() {
		final ViewContext viewContext = getViewContext();
		return FormMode.readOnly.equals(viewContext.get(MODE_CONTEXT_KEY));
	}

	/**
	 * @return Si on est en mode create
	 */
	protected final static boolean isModeCreate() {
		final ViewContext viewContext = getViewContext();
		return FormMode.create.equals(viewContext.get(MODE_CONTEXT_KEY));
	}

	//	/**
	//	 * @return AjaxResponseBuilder pour les requetes Ajax
	//	 */
	//	public final AjaxResponseBuilder createAjaxResponseBuilder() {
	//		//TODO Voir pour l'usage de return AjaxMessage ou FileMessage
	//		try {
	//			response.setCharacterEncoding("UTF-8");
	//			return new AjaxResponseBuilder(response.getWriter(), false);
	//		} catch (final IOException e) {
	//			throw WrappedException.wrap(e, "Impossible de récupérer la response.");
	//		}
	//	}

	//	/**
	//	 * @return VFileResponseBuilder pour l'envoi de fichier
	//	 */
	//	public final VFileResponseBuilder createVFileResponseBuilder() {
	//		return new VFileResponseBuilder(response);
	//	}

	/**
	 * @return Pile des messages utilisateur.
	 */
	public final static SpringMvcUiMessageStack getUiMessageStack() {
		final RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
		final SpringMvcUiMessageStack uiMessageStack = (SpringMvcUiMessageStack) attributes.getAttribute("uiMessageStack", RequestAttributes.SCOPE_REQUEST);
		Assertion.checkNotNull(uiMessageStack);
		//---
		return uiMessageStack;
	}

	public boolean isViewContextDirty() {
		return getViewContext().isDirty();
	}

}
