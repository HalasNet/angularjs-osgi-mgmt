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

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

/**
 * Details about an installed OSGi bundle
 * 
 * @author nils
 * 
 */
public class BundleDetails {

	private BundleInfo bundle;
	private List<ExportedPackage> exportedPackages;
	private List<Requirement> requirements;
	private List<ImportedPackageWiring> importedPackages;
	private List<RequireBundleWiring> requiredBundles;
	private List<GenericWiring> otherWirings;

	private Map<String, String> headers;

	private List<BasicServiceInfo> registeredServices;
	private List<BasicServiceInfo> servicesInUse;

	BundleDetails() {

	}

	public static BundleDetails fromBundle(Bundle bundle) {

		BundleDetails bundleDetails = new BundleDetails();
		bundleDetails.bundle = BundleInfo.fromBundle(bundle);

		bundleDetails.registeredServices = new LinkedList<BasicServiceInfo>();
		bundleDetails.servicesInUse = new LinkedList<BasicServiceInfo>();

		BundleRevision bundleRevision = bundle.adapt(BundleRevision.class);

		// Export-Package
		bundleDetails.exportedPackages = new LinkedList<ExportedPackage>();
		List<BundleCapability> declaredCapabilities = bundleRevision
				.getDeclaredCapabilities(BundleRevision.PACKAGE_NAMESPACE);
		for (BundleCapability bundleCapability : declaredCapabilities) {
			bundleDetails.exportedPackages.add(ExportedPackage
					.fromCapability(bundleCapability));
		}

		// Declared Requirements
		bundleDetails.requirements = new LinkedList<Requirement>();
		List<BundleRequirement> declaredRequirements = bundleRevision
				.getDeclaredRequirements(null);
		for (BundleRequirement bundleRequirement : declaredRequirements) {
			Requirement requirement = Requirement
					.fromBundleRequirement(bundleRequirement);

			bundleDetails.requirements.add(requirement);
		}

		// Import-Package and Require-Bundle
		bundleDetails.importedPackages = new LinkedList<ImportedPackageWiring>();
		bundleDetails.requiredBundles = new LinkedList<RequireBundleWiring>();
		bundleDetails.otherWirings = new LinkedList<GenericWiring>();

		BundleWiring wiring = bundleRevision.getWiring();

		List<BundleWire> requiredWires = wiring.getRequiredWires(null);
		for (BundleWire bundleWire : requiredWires) {

			BundleCapability capability = bundleWire.getCapability();
			System.out.println("cp: " + capability);

			GenericWiring genericWiring = GenericWiring
					.fromCapability(capability);

			if (genericWiring instanceof ImportedPackageWiring) {
				bundleDetails.importedPackages
						.add((ImportedPackageWiring) genericWiring);
			} else if (genericWiring instanceof RequireBundleWiring) {
				bundleDetails.requiredBundles
						.add((RequireBundleWiring) genericWiring);
			} else {
				bundleDetails.otherWirings.add(genericWiring);
			}
		}

		// Registered Services
		ServiceReference<?>[] registeredServices = bundle
				.getRegisteredServices();

		if (registeredServices != null) {

			for (ServiceReference<?> serviceReference : registeredServices) {
				bundleDetails.registeredServices.add(BasicServiceInfo
						.fromServiceReference(serviceReference));
			}
		}

		// Consuming Services

		ServiceReference<?>[] servicesInUse = bundle.getServicesInUse();
		if (servicesInUse != null) {
			for (ServiceReference<?> serviceReference : servicesInUse) {
				bundleDetails.servicesInUse.add(BasicServiceInfo
						.fromServiceReference(serviceReference));
			}
		}

		// Headers
		bundleDetails.headers = new Hashtable<String, String>();
		Dictionary headers = bundle.getHeaders();

		Enumeration keys = headers.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String valueOf = String.valueOf(headers.get(key));

			bundleDetails.headers.put(key, valueOf);

		}

		return bundleDetails;
	}

	public BundleInfo getBundle() {
		return bundle;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
}
