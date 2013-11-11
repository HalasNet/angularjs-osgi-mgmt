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

import javax.servlet.http.HttpServletResponse;

import nh.angularjsosgi.controller.Response;
import nh.angularjsosgi.util.json.JsonService;

/**
 * Default Response implementation
 * 
 * @author nils
 * 
 */
public class ResponseImpl implements Response {

	private final HttpServletResponse _response;

	private final JsonService _jsonService;

	ResponseImpl(HttpServletResponse response, JsonService jsonService) {
		this._response = response;
		this._jsonService = jsonService;
	}

	@Override
	public void send(String body) {
		try {
			_response.getWriter().print(body);
		} catch (Exception er) {
			throw new RuntimeException(er);
		}
	}

	@Override
	public void send(int code) {
		_response.setStatus(code);
	}

	@Override
	public void send(int code, String body) {
		try {
			_response.getWriter().print(body);
			_response.setStatus(code);
		} catch (Exception er) {
			throw new RuntimeException(er);
		}

	}

	@Override
	public void json(Object value) {
		try {
			_jsonService.toJson(_response.getWriter(), value);
		} catch (Exception er) {
			throw new RuntimeException(er);
		}
	}

}
