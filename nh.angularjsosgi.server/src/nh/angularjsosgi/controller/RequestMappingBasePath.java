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
 * Specifes a base path on a controller class that is used as a base for all
 * {@link RequestMapping RequestMappings} that are specified on methods in the
 * controller class
 * 
 * <p>
 * Note that annotations from superclasses are processed too. If there is more
 * than one RequestBasePath specified in a class hierachy the one on the most
 * concrete class wins
 * 
 * @author nils
 * 
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMappingBasePath {

	/**
	 * Format: path
	 * 
	 * @return
	 */
	String value();

}
