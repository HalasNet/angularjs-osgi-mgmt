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
 * Represents a Reponse of a Client Request
 * 
 * <p>
 * This interface can be used to send content and/or status codes back to the
 * client
 * 
 * <p>
 * Instances of Reponse can be used in Controller's RequestMapping methods as a
 * Parameter
 * 
 * @author nils
 * 
 */
public interface Response {

	public void send(String body);

	public void send(int code, String body);

	public void send(int code);

	/**
	 * Send the specified object in JSON Format back to the client
	 * 
	 * @param value
	 */
	public void json(Object value);

}
