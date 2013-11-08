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
package nh.angularjsosgi.rest.osgi.model;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Abstract base class for REST Reponse Objects.
 * 
 * <p>
 * Provides some util methods
 * 
 * @author nils
 * 
 */
public class AbstractRestObject {

	protected AbstractRestObject() {

	}

	/**
	 * converts the specified object to a string. If o is an array a
	 * comma-separated list with all entries of o will be returned
	 * 
	 * @param o
	 * @return
	 */
	protected static String toString(Object o) {
		if (o instanceof String[]) {
			String[] objects = (String[]) o;
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < objects.length; i++) {
				builder.append(String.valueOf(objects[i]));
				if (i < objects.length - 1) {
					builder.append(',');
				}
			}

			return builder.toString();
		}

		return String.valueOf(o);
	}

	/**
	 * converts the specified map to a map that consists of String values only
	 * 
	 * @param attributes
	 * @return
	 */
	protected static Map<String, String> convertToAttributes(
			Map<String, Object> attributes) {
		Set<Entry<String, Object>> entrySet = attributes.entrySet();

		Map<String, String> result = new Hashtable<String, String>();
		for (Entry<String, Object> entry : entrySet) {
			result.put(entry.getKey(), toString(entry.getValue()));
		}

		return result;

	}

}
