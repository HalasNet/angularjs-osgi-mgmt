/*******************************************************************************
 * Copyright (c) 2013 Nils Hartmann (http://nilshartmann.net).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann (nils@nilshartmann.net) - initial API and implementation
 ******************************************************************************/
package nh.eangularjsosgi.server.router;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import nh.angularjsosgi.controller.Controller;
import nh.angularjsosgi.controller.RequestMapping;
import nh.angularjsosgi.controller.RequestMappingBasePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.AntPathMatcher;
import org.springframework.web.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

/**
 * A Router routes requests to appropriate handler methods on registered
 * Controllers
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
@Component(provide = Router.class)
public class Router {

	/** The logger */
	Logger _logger = LoggerFactory.getLogger(getClass());

	private UrlPathHelper _urlPathHelper = new UrlPathHelper();

	private PathMatcher _pathMatcher = new AntPathMatcher();

	/**
	 * Known Controllers
	 */
	private final ConcurrentHashMap<Controller, ControllerHandler> _registeredController = new ConcurrentHashMap<Controller, ControllerHandler>();

	/**
	 * Finds a Method that can handle the given request
	 * 
	 * @param request
	 * @return a handler method or null if no method can be found
	 * @throws Exception
	 */
	public RequestMethod getRequestHandler(HttpServletRequest request)
			throws Exception {

		String lookupPath = _urlPathHelper.getLookupPathForRequest(request);
		String httpMethod = request.getMethod();
		_logger.info("Find Handler for path '{}' and method '{}'", lookupPath,
				httpMethod);

		Collection<ControllerHandler> controllerHandler = getControllerHandler();

		// get candidates that might be able to handle this request
		final List<RequestSpecificMethodInfo> requestSpecificMethodInfos = getAllCandidateHandlers(
				lookupPath, httpMethod);

		// no method found
		if (requestSpecificMethodInfos.isEmpty()) {
			return null;
		}

		// Determine best matching method
		RequestSpecificMethodInfoComparator comparator = new RequestSpecificMethodInfoComparator(
				_pathMatcher.getPatternComparator(lookupPath));
		Collections.sort(requestSpecificMethodInfos, comparator);

		// create RequestMethod
		RequestSpecificMethodInfo requestSpecificMethodInfo = requestSpecificMethodInfos
				.get(0);
		_logger.debug("requestSpecificMethodInfo found: "
				+ requestSpecificMethodInfo);

		// return the method

		return requestSpecificMethodInfo;
	}

	protected List<RequestSpecificMethodInfo> getAllCandidateHandlers(
			String lookupPath, String httpMethod) {
		Collection<ControllerHandler> controllerHandler = getControllerHandler();

		final List<RequestSpecificMethodInfo> requestSpecificMethodInfos = new LinkedList<RequestSpecificMethodInfo>();

		// Alle Controller nach ihren Methoden fragen, die auf diesen Request
		// passen
		for (ControllerHandler handler : controllerHandler) {
			List<RequestSpecificMethodInfo> methods = handler.getMethods(
					lookupPath, httpMethod);
			requestSpecificMethodInfos.addAll(methods);
		}

		return requestSpecificMethodInfos;

	}

	protected Collection<ControllerHandler> getControllerHandler() {
		return _registeredController.values();
	}

	@Reference(dynamic = true, multiple = true, type = '*')
	public void bindController(Controller controller) throws Exception {
		_logger.info("Register controller: " + controller);
		ControllerHandler controllerHandler = new ControllerHandler(controller);
		try {
			controllerHandler.init();
		} catch (Exception ex) {
			_logger.error("Could not create ControllerHandler for "
					+ controller + ": " + ex, ex);
			throw ex;
		}
		_registeredController.put(controller, controllerHandler);
	}

	public void unbindController(Controller controller) {

		_logger.info("Unregister controller: " + controller);
		_registeredController.remove(controller);

	}

	// -------------------------------------------------------------------------------------------

	class ControllerHandler {

		private final Controller _controller;

		private final List<RequestMethod> _handlerMethods = new LinkedList<RequestMethod>();

		/**
		 * @param controller
		 */
		public ControllerHandler(Controller controller) {
			this._controller = controller;

		}

		/**
		 * @return
		 */
		protected List<RequestSpecificMethodInfo> getMethods(String lookupPath,
				String httpMethod) {

			List<RequestSpecificMethodInfo> candidates = new LinkedList<Router.RequestSpecificMethodInfo>();

			for (RequestMethod requestMethodInfo : _handlerMethods) {

				if (!requestMethodInfo.isHttpMethodSupported(httpMethod)) {
					continue;
				}

				String pattern = requestMethodInfo.getPattern();

				String matchingPattern = getMatchingPattern(pattern,
						lookupPath, false);
				if (matchingPattern != null) {
					RequestSpecificMethodInfo requestSpecificMethodInfo = new RequestSpecificMethodInfo(
							requestMethodInfo, matchingPattern);
					candidates.add(requestSpecificMethodInfo);
				}
			}

			return candidates;

		}

		private String getMatchingPattern(String pattern, String lookupPath,
				boolean useSuffixPattern) {
			if (pattern.equals(lookupPath)) {
				return pattern;
			}
			boolean hasSuffix = pattern.indexOf('.') != -1;
			if (useSuffixPattern && !hasSuffix) {
				String patternWithSuffix = pattern + ".*";
				if (_pathMatcher.match(patternWithSuffix, lookupPath)) {
					return patternWithSuffix;
				}
			}
			if (_pathMatcher.match(pattern, lookupPath)) {
				return pattern;
			}
			boolean endsWithSlash = pattern.endsWith("/");
			if (useSuffixPattern && !endsWithSlash) {
				String patternWithSlash = pattern + "/";
				if (_pathMatcher.match(patternWithSlash, lookupPath)) {
					return patternWithSlash;
				}
			}
			return null;
		}

		public void init() throws Exception {
			final Class<?> targetClass = _controller.getClass();

			final String requestMappingBasePath = getRequestBase(targetClass);

			ReflectionUtils.doWithMethods(targetClass,
					new ReflectionUtils.MethodCallback() {
						@Override
						public void doWith(Method method) {
							Method specificMethod = ClassUtils
									.getMostSpecificMethod(method, targetClass);
							Method bridgedMethod = BridgeMethodResolver
									.findBridgedMethod(specificMethod);
							if (isHandlerMethod(specificMethod)
									&& (bridgedMethod == specificMethod || !isHandlerMethod(bridgedMethod))) {
								addHandlerMethod(requestMappingBasePath,
										specificMethod);
							}
						}
					}, ReflectionUtils.USER_DECLARED_METHODS);
		}

		private String getRequestBase(Class<?> targetClass) {

			RequestMappingBasePath requestBasePath = AnnotationUtils
					.findAnnotation(targetClass, RequestMappingBasePath.class);

			if (requestBasePath != null
					&& StringUtils.hasText(requestBasePath.value())) {
				return requestBasePath.value().trim();
			}

			return null;

		}

		protected String combine(String pattern1, String pattern2) {
			if (!StringUtils.hasText(pattern1)
					&& !StringUtils.hasText(pattern2)) {
				return "";
			} else if (!StringUtils.hasText(pattern1)) {
				return pattern2;
			} else if (!StringUtils.hasText(pattern2)) {
				return pattern1;
			}

			if (pattern1.endsWith("/") && pattern2.startsWith("/")) {
				if (pattern1.length() == 1) {
					return pattern2;
				}

				return pattern1.substring(1) + pattern2;
			}

			if (pattern1.endsWith("/") || pattern2.startsWith("/")) {
				return pattern1 + pattern2;
			}

			return pattern1 + "/" + pattern2;
		}

		protected void addHandlerMethod(String requestMappingBasePath,
				Method method) {
			RequestMapping requestMapping = AnnotationUtils.findAnnotation(
					method, RequestMapping.class);
			String mapping = requestMapping.value();
			String[] items = mapping.split(" ");

			String pattern = null;
			String[] httpMethods = null;
			if (items.length == 1) {
				// alle HTTP-Methoden erlaubt
				pattern = items[0];
			} else if (items.length == 2) {
				pattern = combine(requestMappingBasePath, items[1]);

				httpMethods = items[0].split(",");
				for (int i = 0; i < httpMethods.length; i++) {
					if (httpMethods[i].trim().equals("*")) {
						httpMethods = null;
						break;
					}
					httpMethods[i] = httpMethods[i].trim();
				}
			} else {
				throw new IllegalStateException("Invalid Request Mapping '"
						+ mapping + "'");
			}

			RequestMethod requestMethodInfo = new RequestMethod(_controller,
					method, pattern, httpMethods);
			_handlerMethods.add(requestMethodInfo);

		}

		protected boolean isHandlerMethod(Method method) {
			return AnnotationUtils.findAnnotation(method, RequestMapping.class) != null;
		}

	}

	class RequestSpecificMethodInfo extends RequestMethod {
		private final String _matchedPattern;

		RequestSpecificMethodInfo(RequestMethod other, String matchingPattern) {
			super(other._controller, other._method, other._pattern,
					other._httpMethods);

			this._matchedPattern = matchingPattern;
		}

		/**
		 * @return the matchedPattern
		 */
		public String getMatchedPattern() {
			return _matchedPattern;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "RequestSpecificMethodInfo [_matchedPattern="
					+ _matchedPattern + ", getMethod()=" + getMethod()
					+ ", getPattern()=" + getPattern() + ", getHttpMethods()="
					+ Arrays.toString(getHttpMethods()) + "]";
		}

	}

	static class RequestSpecificMethodInfoComparator implements
			Comparator<RequestSpecificMethodInfo> {

		private final Comparator<String> _pathComparator;

		public RequestSpecificMethodInfoComparator(
				Comparator<String> pathComparator) {
			this._pathComparator = pathComparator;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(RequestSpecificMethodInfo info1,
				RequestSpecificMethodInfo info2) {
			int pathComparison = _pathComparator.compare(
					info1.getMatchedPattern(), info2.getMatchedPattern());
			if (pathComparison != 0) {
				return pathComparison;
			}

			int info1MethodCount = info1.getHttpMethodCount();
			int info2MethodCount = info2.getHttpMethodCount();
			if (info1MethodCount == 0 && info2MethodCount > 0) {
				return 1;
			} else if (info2MethodCount == 0 && info1MethodCount > 0) {
				return -1;
			} else if (info1MethodCount == 1 & info2MethodCount > 1) {
				return -1;
			} else if (info2MethodCount == 1 & info1MethodCount > 1) {
				return 1;
			}
			return 0;
		}

	}

}
