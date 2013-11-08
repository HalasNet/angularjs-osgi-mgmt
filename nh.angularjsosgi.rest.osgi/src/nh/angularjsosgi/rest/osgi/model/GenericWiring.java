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

import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;

/**
 * An existing wiring. The wiring can either be a Package-, Bundle or other
 * "untyped" wiring.
 * 
 * @author nils
 * 
 */
public class GenericWiring extends Capability {

	/**
	 * The bundle that provides this wiring
	 */
	protected InstalledBundleId provider;

	public static GenericWiring fromCapability(BundleCapability capability) {

		GenericWiring genericWiring = null;

		if (capability.getAttributes().containsKey(
				BundleRevision.PACKAGE_NAMESPACE)) {
			ImportedPackageWiring importedPackageWiring = new ImportedPackageWiring();
			ImportedPackageWiring.setupFromCapability(importedPackageWiring,
					capability);
			genericWiring = importedPackageWiring;
		} else if (capability.getAttributes().containsKey(
				BundleRevision.BUNDLE_NAMESPACE)) {
			RequireBundleWiring requireBundleWiring = new RequireBundleWiring();
			RequireBundleWiring.setupFromCapability(requireBundleWiring,
					capability);
			genericWiring = requireBundleWiring;
		} else {
			genericWiring = new GenericWiring();
		}

		genericWiring.provider = InstalledBundleId.fromBundle(capability
				.getRevision().getBundle());
		genericWiring.directives = new Hashtable<String, String>(
				capability.getDirectives());
		genericWiring.attributes = convertToAttributes(capability
				.getAttributes());

		return genericWiring;
	}

	public InstalledBundleId getProvider() {
		return provider;
	}
}
