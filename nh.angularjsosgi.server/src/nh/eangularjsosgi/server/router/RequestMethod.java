/*******************************************************************************
 * Copyright (c) 2011 Nils Hartmann
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Nils Hartmann - initial API and implementation
 ******************************************************************************/
package nh.eangularjsosgi.server.router;

import java.lang.reflect.Method;

import nh.angularjsosgi.controller.Controller;

/**
 * A Method on a controller that is able to handle a request
 * 
 * @author nils
 * 
 */
public class RequestMethod {

	final Controller _controller;

	final Method _method;

	final String _pattern;

	final String[] _httpMethods;

	/**
	 * @param method
	 * @param pattern
	 * @param httpMethods
	 */
	public RequestMethod(Controller controller, Method method, String pattern,
			String[] httpMethods) {
		super();
		this._controller = controller;
		_method = method;
		_pattern = pattern;
		_httpMethods = httpMethods;
	}

	/**
	 * /**
	 * 
	 * @return the controller
	 */
	public Controller getController() {
		return _controller;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return _method;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return _pattern;
	}

	/**
	 * @return the httpMethods
	 */
	public String[] getHttpMethods() {
		return _httpMethods;
	}

	public int getHttpMethodCount() {
		if (_httpMethods == null) {
			return 0;
		}

		return _httpMethods.length;
	}

	public boolean isHttpMethodSupported(String httpMethod) {
		if (_httpMethods == null || _httpMethods.length == 0) {
			// alle erlaubt
			return true;
		}

		for (String supportedMethod : _httpMethods) {
			if (supportedMethod.equalsIgnoreCase(httpMethod)) {
				return true;
			}
		}

		return false;
	}
}