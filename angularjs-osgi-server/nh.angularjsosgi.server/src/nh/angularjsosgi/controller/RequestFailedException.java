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
package nh.angularjsosgi.controller;

/**
 * RuntimeException thrown by Controllers etc. The code will be used as HTTP
 * Response code.
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
public class RequestFailedException extends RuntimeException {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	private final int _code;

	public RequestFailedException(String message, int code) {
		super(message);

		this._code = code;
	}

	public RequestFailedException(int code) {
		this._code = code;
	}

	public int getCode() {
		return _code;
	}

}
