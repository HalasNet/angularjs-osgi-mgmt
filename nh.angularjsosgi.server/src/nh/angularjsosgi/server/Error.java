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

/**
 * An object send back (in JSON Format) to the client in case a request failed
 * with an Exception
 * 
 * @author nils
 * 
 */
class Error {

	private final int _code;
	private final String _message;

	Error(int code, String message) {
		this._code = code;
		this._message = message;
	}

	public int getCode() {
		return _code;
	}

	public String getMessage() {
		return _message;
	}

}
