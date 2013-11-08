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

import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * Basic Informations about a registered OSGi Service
 * 
 * @author nils
 * 
 */
public class BasicServiceInfo extends AbstractRestObject {

	private long id;
	private String pid;
	private String description;
	private String vendor;
	private List<String> serviceInterfaces;
	private InstalledBundleId provider;
	private int usingBundlesCount;

	public static BasicServiceInfo fromServiceReference(ServiceReference<?> ref) {

		BasicServiceInfo info = new BasicServiceInfo();
		populateFromServiceReference(info, ref);
		return info;
	}

	protected static void populateFromServiceReference(BasicServiceInfo info,
			ServiceReference<?> ref) {

		info.id = (Long) ref.getProperty(Constants.SERVICE_ID);
		info.pid = (String) ref.getProperty(Constants.SERVICE_PID);
		info.description = (String) ref
				.getProperty(Constants.SERVICE_DESCRIPTION);
		info.vendor = (String) ref.getProperty(Constants.SERVICE_VENDOR);

		info.serviceInterfaces = new LinkedList<String>();

		String[] objectClass = (String[]) ref
				.getProperty(Constants.OBJECTCLASS);
		for (String string : objectClass) {
			info.serviceInterfaces.add(string);
		}
		info.provider = InstalledBundleId.fromBundle(ref.getBundle());
		Bundle[] usingBundles = ref.getUsingBundles();
		info.usingBundlesCount = usingBundles == null ? 0 : usingBundles.length;

	}

	public List<String> getServiceInterfaces() {
		return serviceInterfaces;
	}

	public InstalledBundleId getProvider() {
		return provider;
	}

	public int getUsingBundlesCount() {
		return usingBundlesCount;
	}

	public long getId() {
		return id;
	}

	public String getPid() {
		return pid;
	}

	public String getVendor() {
		return vendor;
	}

	public String getDescription() {
		return description;
	}

}
