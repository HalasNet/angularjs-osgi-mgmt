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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * Details of a registered OSGi service
 * 
 * @author nils
 * 
 */
public class ServiceDetails extends BasicServiceInfo {

	/**
	 * List of all bundles that are using this service currently
	 */
	private List<InstalledBundleId> usingBundles;

	/**
	 * The service properties
	 */
	private Map<String, String> properties;

	public static ServiceDetails fromServiceReference(ServiceReference<?> ref) {
		ServiceDetails details = new ServiceDetails();
		populateFromServiceReference(details, ref);
		populateDetailsFromServiceReference(details, ref);

		return details;
	}

	protected static void populateDetailsFromServiceReference(
			ServiceDetails details, ServiceReference<?> ref) {

		details.usingBundles = new LinkedList<InstalledBundleId>();

		Bundle[] usingBundles = ref.getUsingBundles();
		if (usingBundles != null) {
			for (Bundle bundle : usingBundles) {
				InstalledBundleId usingBundle = InstalledBundleId
						.fromBundle(bundle);
				details.usingBundles.add(usingBundle);
			}
		}

		details.properties = new Hashtable<String, String>();

		String[] propertyKeys = ref.getPropertyKeys();

		for (String string : propertyKeys) {
			if (Constants.SERVICE_ID.equals(string)
					|| Constants.SERVICE_PID.equals(string)
					|| Constants.SERVICE_VENDOR.equals(string)
					|| Constants.OBJECTCLASS.equals(string)
					|| Constants.SERVICE_DESCRIPTION.equals(string)) {
				continue;
			}

			details.properties.put(string, toString(ref.getProperty(string)));

		}

	}

}
