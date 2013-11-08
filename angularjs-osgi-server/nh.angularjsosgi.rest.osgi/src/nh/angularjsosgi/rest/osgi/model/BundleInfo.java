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
 * Basic informations about an installed OSGi bundle
 * 
 * @author nils
 * 
 */
public class BundleInfo extends InstalledBundleId {

	private String name;

	private String state;
	private int servicesInUse;
	private int servicesProvided;
	private String location;

	BundleInfo() {

	}

	public static BundleInfo fromBundle(Bundle bundle) {

		BundleInfo bundleInfo = new BundleInfo();

		setupFromBundle(bundleInfo, bundle);

		bundleInfo.name = bundle.getHeaders().get(Constants.BUNDLE_NAME);
		bundleInfo.servicesInUse = (bundle.getServicesInUse() != null ? bundle
				.getServicesInUse().length : 0);
		bundleInfo.servicesProvided = (bundle.getRegisteredServices() != null ? bundle
				.getRegisteredServices().length : 0);
		bundleInfo.location = bundle.getLocation();

		String bundleState = "unknown";

		switch (bundle.getState()) {
		case Bundle.ACTIVE:
			bundleState = "Active";
			break;
		case Bundle.INSTALLED:
			bundleState = "Installed";
			break;
		case Bundle.RESOLVED:
			bundleState = "Resolved";
			break;
		case Bundle.STARTING:
			bundleState = "Starting";
			break;
		case Bundle.STOPPING:
			bundleState = "Stopping";
			break;
		case Bundle.UNINSTALLED:
			bundleState = "Uninstalled";
			break;
		default:
			bundleState = "unknown: " + bundle.getState();
		}
		bundleInfo.state = bundleState;

		return bundleInfo;

	}

	public int getServicesInUse() {
		return servicesInUse;
	}

	public String getState() {
		return state;
	}

	public String getName() {
		return name;
	}

	public int getServicesProvided() {
		return servicesProvided;
	}

	public String getLocation() {
		return location;
	}

}
