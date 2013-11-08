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

import org.osgi.framework.Constants;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;

/**
 * A package that is exported by a bundle
 * 
 * @author nils
 * 
 */
public class ExportedPackage extends Capability {

	private String name;
	private String version;

	public static ExportedPackage fromCapability(
			BundleCapability bundleCapability) {
		ExportedPackage exportedPackage = new ExportedPackage();
		exportedPackage.directives = new Hashtable<String, String>(
				bundleCapability.getDirectives());
		exportedPackage.attributes = new Hashtable<String, String>(); // not
																		// used

		Map<String, Object> attributes = bundleCapability.getAttributes();
		exportedPackage.name = (String) attributes
				.get(BundleRevision.PACKAGE_NAMESPACE);
		exportedPackage.version = String.valueOf(attributes
				.get(Constants.VERSION_ATTRIBUTE));

		return exportedPackage;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

}
