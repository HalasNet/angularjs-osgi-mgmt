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
package nh.angularjsosgi.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a Method on a Controller as a Request handling method
 * 
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

	/**
	 * Format: METHOD path, e.g. "GET /hello" or "POST /hello"
	 * 
	 * <p>
	 * It's possible to specify varibales in the the path, that are passed as
	 * arguments to the method, e.g. "GET /hello/{name}". "Name" would be passed
	 * to an argument called name in this example
	 * 
	 * @return
	 */
	String value();

}
