package io.vertigo.ui.impl.springmvc.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;

import io.vertigo.app.Home;
import io.vertigo.commons.analytics.AnalyticsManager;
import io.vertigo.lang.WrappedException;

public class VSpringDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = 1836587973110870111L;

	private AnalyticsManager analyticsManager;

	public VSpringDispatcherServlet(final WebApplicationContext webApplicationContext) {
		super(webApplicationContext);
	}

	@Override
	protected void doDispatch(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final HandlerExecutionChain handlerChain = getHandler(request);
		if (handlerChain != null) {
			final Object handler = handlerChain.getHandler();
			if (handler instanceof HandlerMethod) {
				final HandlerMethod handlerMethod = (HandlerMethod) handler;
				final RequestMapping beanRequestMapping = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), RequestMapping.class);
				final RequestMapping methodRequestMapping = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), RequestMapping.class);
				final String path = beanRequestMapping.path()[0] + methodRequestMapping.path()[0];
				getAnalyticsManager().trace(
						"page",
						path,
						tracer -> {
							try {
								super.doDispatch(request, response);
							} catch (final Exception e) {
								throw WrappedException.wrap(e);
							}
						});
			}

		} else {
			super.doDispatch(request, response);
		}

	}

	private AnalyticsManager getAnalyticsManager() {
		if (analyticsManager == null) {
			setAnalyticsManager();
		}
		return analyticsManager;
	}

	private synchronized void setAnalyticsManager() {
		analyticsManager = Home.getApp().getComponentSpace().resolve(AnalyticsManager.class);
	}

}
