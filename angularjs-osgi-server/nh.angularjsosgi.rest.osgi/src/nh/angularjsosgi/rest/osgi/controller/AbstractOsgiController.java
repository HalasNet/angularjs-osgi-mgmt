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
package nh.angularjsosgi.rest.osgi.controller;

import java.util.Hashtable;
import java.util.Map;

import nh.angularjsosgi.controller.Controller;
import nh.angularjsosgi.controller.RequestMappingBasePath;

/**
 * Base class for OSGi Management Rest Controller.
 * 
 * @author nils
 * 
 */
@RequestMappingBasePath("/v1")
public class AbstractOsgiController extends Controller {

	/**
	 * 
	 * @param string
	 * @return
	 */
	protected long asLong(String string) {
		try {
			return Long.parseLong(string.toString());
		} catch (Exception ex) {
			throw badRequest("Parameter value: '" + string
					+ "' cannot be parsed to Long: " + ex);
		}
	}

	/**
	 * Creates a new Map from the specified key and value
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	protected Object keyValue(String key, Object value) {
		Map<String, Object> map = new Hashtable<String, Object>();

		map.put(key, value);

		return map;
	}

}
