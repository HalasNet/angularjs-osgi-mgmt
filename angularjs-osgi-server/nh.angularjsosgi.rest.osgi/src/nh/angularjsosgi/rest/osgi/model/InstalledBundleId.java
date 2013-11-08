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

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

/**
 * A "extended" id of an installed bundle
 * 
 * @author nils
 * 
 */
public class InstalledBundleId {

	protected long id;
	protected String symbolicName;
	protected String version;

	protected static void setupFromBundle(InstalledBundleId bundleInfo,
			Bundle bundle) {
		bundleInfo.id = bundle.getBundleId();
		bundleInfo.symbolicName = bundle.getSymbolicName();
		bundleInfo.version = bundle.getHeaders().get(Constants.BUNDLE_VERSION);

	}

	public long getId() {
		return id;
	}

	public String getSymbolicName() {
		return symbolicName;
	}

	public String getVersion() {
		return version;
	}

	public static InstalledBundleId fromBundle(Bundle bundle) {

		InstalledBundleId installedBundleId = new InstalledBundleId();
		setupFromBundle(installedBundleId, bundle);

		return installedBundleId;
	}

}
