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

import org.osgi.framework.Constants;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;

/**
 * A wiring caused by a Require-Package header
 * 
 * @author nils
 * 
 */
public class RequireBundleWiring extends GenericWiring {

	/** Same as the providing bundle, but more explizit here */
	protected String requiredBundleSymbolicName;
	/** Same as the providing bundle, but more explizit here */
	protected String requiredBundleVersion;

	protected static void setupFromCapability(
			RequireBundleWiring requireBundleWiring, BundleCapability capability) {

		requireBundleWiring.requiredBundleVersion = String.valueOf(capability
				.getAttributes().get(Constants.BUNDLE_VERSION_ATTRIBUTE));
		requireBundleWiring.requiredBundleSymbolicName = String
				.valueOf(capability.getAttributes().get(
						BundleRevision.BUNDLE_NAMESPACE));

	}

}
