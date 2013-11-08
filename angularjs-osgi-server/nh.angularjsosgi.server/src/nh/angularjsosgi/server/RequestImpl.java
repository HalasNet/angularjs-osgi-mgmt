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

import javax.servlet.http.HttpServletRequest;

import nh.angularjsosgi.controller.Request;

/**
 * Default Request implementation
 * 
 * @author nils
 * 
 */
public class RequestImpl implements Request {

	private final HttpServletRequest _request;

	public RequestImpl(HttpServletRequest request) {
		this._request = request;
	}

	@Override
	public String getHeader(String name) {
		return _request.getHeader(name);
	}

	@Override
	public String getQueryParameter(String name) {
		String queryParameter = _request.getParameter(name);

		return queryParameter;
	}

}
