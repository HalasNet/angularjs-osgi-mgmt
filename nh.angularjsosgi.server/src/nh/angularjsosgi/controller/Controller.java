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

import javax.servlet.http.HttpServletResponse;

import nh.angularjsosgi.http.RequestErrors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Controller handles one or more HTTP Requests.
 * 
 * <p>
 * Instances must be registered at the OSGi Service Registry with the
 * Controller.class as Service interfaces
 * <p>
 * Methods that handles requests must be annotated with {@link RequestMapping}
 * 
 * @author nils
 * 
 */
public class Controller extends RequestErrors {

	protected final Logger _logger = LoggerFactory.getLogger(getClass());

	/**
	 * Return code "OK, but no content"
	 */
	public static final int OK_NO_CONTENT = HttpServletResponse.SC_NO_CONTENT;
	/**
	 * Return code "OK"
	 */
	public static final int OK = HttpServletResponse.SC_OK;

}
