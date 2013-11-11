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
package nh.angularjsosgi.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nh.angularjsosgi.controller.Controller;
import nh.angularjsosgi.controller.Request;
import nh.angularjsosgi.controller.Response;
import nh.angularjsosgi.http.RequestErrors;
import nh.angularjsosgi.http.RequestFailedException;
import nh.angularjsosgi.util.json.JsonService;
import nh.eangularjsosgi.server.router.RequestMethod;
import nh.eangularjsosgi.server.router.Router;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriTemplate;
import org.springframework.web.util.UrlPathHelper;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

/**
 * Main HttpServlet that handles requests for the REST Api.
 * 
 * <p>
 * The actual processing of a request is delegated to a {@link Controller}
 * instance.
 * 
 * @author nils
 * 
 */
@Component(immediate = true)
public class ApiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Logger _logger = LoggerFactory.getLogger(getClass());

	public final static String CONTEXT_PATH = "/api";

	/** Router used to map requests to controllers */
	private Router _router;

	/** JSon */
	private JsonService _jsonService;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		_logger.info("Path invoked '{}'", pathInfo);

		allowCrossDomainScripting(response);

		// Determine method that handles this request
		RequestMethod requestHandler = null;
		try {
			requestHandler = _router.getRequestHandler(req);
		} catch (Exception ex) {
			_logger.error("Could not find request handler: " + ex, ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ex.toString());

			return;
		}

		if (requestHandler == null) {
			response.sendError(404);

			return;
		}

		try {
			invoke(requestHandler, req, response);
		} catch (Exception ex) {
			_logger.error("Request Failed: " + ex, ex);
			int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			if (ex instanceof RequestFailedException) {
				RequestFailedException rfe = (RequestFailedException) ex;
				status = rfe.getCode();
			}

			_jsonService.toJson(response.getWriter(),
					new Error(status, ex.getMessage()));
			response.setStatus(status);

			return;
		}

	}

	private void allowCrossDomainScripting(HttpServletResponse response) {
		// TODO: Restrict...
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods",
				"GET,PUT,POST,DELETE");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type");

	}

	protected void invoke(RequestMethod requestMethod,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Parse Request
		UriTemplate uriTemplate = new UriTemplate(requestMethod.getPattern());
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		String lookupPath = urlPathHelper.getLookupPathForRequest(request);

		// Create Request and Response Object for Controller
		RequestImpl requestImpl = new RequestImpl(request);
		ResponseImpl responseImpl = new ResponseImpl(response, _jsonService);

		// Determine Target Method
		Method targetMethod = requestMethod.getMethod();
		Class<?>[] parameterTypes = targetMethod.getParameterTypes();

		List<Object> arguments = new LinkedList<Object>();

		Map<String, String> variables = uriTemplate.match(lookupPath);
		List<String> variableNames = uriTemplate.getVariableNames();

		Iterator<String> variableNamesIterator = variableNames.iterator();

		// setup method invocation: bind required parameters from request to
		// method arguments
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> expectedType = parameterTypes[i];
			if (Response.class.isAssignableFrom(expectedType)) {
				arguments.add(responseImpl);
			} else if (Request.class.isAssignableFrom(expectedType)) {
				arguments.add(requestImpl);
			} else {
				if (!variableNamesIterator.hasNext()) {
					if (parameterTypes.length != variableNames.size() + 1) {
						throw new IllegalStateException("Method '"
								+ targetMethod
								+ "' does not match parameter count ("
								+ variableNames.size() + ") in UriTemplate");
					}
				}

				String variableName = variableNamesIterator.next();
				String value = variables.get(variableName);
				Object convertedValue = convertValue(value, expectedType);
				arguments.add(convertedValue);
			}
		}

		// invoke method on controller
		try {
			Controller controller = requestMethod.getController();
			Object[] array = arguments.toArray();
			requestMethod.getMethod().invoke(controller, array);
		} catch (Exception ex) {
			if (ex instanceof InvocationTargetException) {
				InvocationTargetException ite = (InvocationTargetException) ex;
				Throwable cause = ite.getCause();
				if (cause instanceof RequestFailedException) {
					throw (RequestFailedException) cause;
				}
			}

			throw ex;
		}

	}

	private Object convertValue(String value, Class<?> expectedType) {
		if (expectedType == int.class) {
			try {
				return Integer.valueOf(value);
			} catch (Exception ex) {
				throw RequestErrors
						.badRequest("Invalid parameter. Expected Type: int.");
			}
		}
		if (expectedType == String.class) {
			return value;
		}

		if (expectedType == long.class) {
			try {
				return Long.valueOf(value);
			} catch (Exception ex) {
				throw RequestErrors
						.badRequest("Invalid parameter. Expected Type: long.");
			}
		}

		try {
			Constructor<?> cstr = expectedType.getConstructor(String.class);
			return cstr.newInstance(value);
		} catch (Exception ex) {
			String msg = String.format(
					"Could not create instance of type '%s'. Reason: %s",
					expectedType.getName(), ex);
			_logger.error(msg, ex);
			throw RequestErrors.badRequest(msg);
		}

	}

	// =====================================================================
	// === SERVICE BINDING
	// =====================================================================
	@Reference(dynamic = true, multiple = true)
	public void bindHttpService(HttpService httpService)
			throws ServletException, NamespaceException {

		_logger.info("binding http service: " + httpService);
		httpService.registerServlet(CONTEXT_PATH, this, null, null);
	}

	public void unbindHttpService(HttpService httpService)
			throws ServletException, NamespaceException {
		_logger.info("unbind http service: " + httpService);
		try {
			httpService.unregister(CONTEXT_PATH);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Reference
	public void setRouter(Router router) {
		_router = router;
	}

	@Reference
	public void setJsonService(JsonService jsonService) {
		_jsonService = jsonService;
	}

}
