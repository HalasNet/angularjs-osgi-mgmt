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
 * A wiring defined by an imported package
 * 
 * @author nils
 * 
 */
public class ImportedPackageWiring extends GenericWiring {

	protected String packageName;
	protected String importedPackageVersion;

	protected static void setupFromCapability(
			ImportedPackageWiring importedPackageWiring,
			BundleCapability capability) {

		importedPackageWiring.importedPackageVersion = String
				.valueOf(capability.getAttributes().get(
						Constants.VERSION_ATTRIBUTE));
		importedPackageWiring.packageName = String.valueOf(capability
				.getAttributes().get(BundleRevision.PACKAGE_NAMESPACE));

	}

}
