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

import org.osgi.framework.wiring.BundleRequirement;

/**
 * A Requirement defined on a OSGi bundle
 * 
 * @author nils
 * 
 */
public class Requirement {

	private Map<String, String> attributes;
	private Map<String, String> directives;

	public static Requirement fromBundleRequirement(
			BundleRequirement bundleRequirement) {
		Requirement requirement = new Requirement();
		requirement.directives = new Hashtable<String, String>(
				bundleRequirement.getDirectives());
		Set<Entry<String, Object>> entrySet = bundleRequirement.getAttributes()
				.entrySet();

		requirement.attributes = new Hashtable<String, String>();
		for (Entry<String, Object> entry : entrySet) {
			requirement.attributes.put(entry.getKey(),
					String.valueOf(entry.getValue()));
		}

		return requirement;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public Map<String, String> getDirectives() {
		return directives;
	}

}
