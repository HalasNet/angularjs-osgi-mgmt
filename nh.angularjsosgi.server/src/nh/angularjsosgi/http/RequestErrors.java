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
package nh.angularjsosgi.http;

import javax.servlet.http.HttpServletResponse;

/**
 * Common Errors
 * 
 * @author nils
 * 
 */
public class RequestErrors {
	/**
	 * Indicating a Bad Request (should be used when Request is invalid, e.g.
	 * invalid parameter)
	 * 
	 * @param message
	 * @return
	 */
	public static RequestFailedException badRequest(String message) {
		return new RequestFailedException(message,
				HttpServletResponse.SC_BAD_REQUEST);
	}

	/**
	 * Not Found (404), in case a requested entity or controller can not be
	 * found
	 * 
	 * @return
	 */
	public static RequestFailedException notFound() {
		return new RequestFailedException(HttpServletResponse.SC_NOT_FOUND);
	}

	/**
	 * Not Found (404), in case a requested entity can not be found
	 * 
	 * @return
	 */
	public static RequestFailedException entityNotFound(String type, String id) {
		return new RequestFailedException(String.format(
				"Entity '%s' with id '%s' not found", type, id),
				HttpServletResponse.SC_NOT_FOUND);
	}

}
