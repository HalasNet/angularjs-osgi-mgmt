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
package nh.angularjsosgi.rest.osgi.felix;

import java.util.LinkedList;
import java.util.List;

import org.apache.felix.scr.Reference;
import org.osgi.framework.ServiceReference;

import nh.angularjsosgi.rest.osgi.model.BasicServiceInfo;

/**
 * A references to another DS component
 * 
 * @author nils
 * 
 */
public class ScrReference {

	private boolean satisfied;

	private String serviceInterface;

	private String targetFilter;

	private boolean multiple;
	private boolean optional;
	private boolean dynamic;

	private List<BasicServiceInfo> boundServices;

	public static ScrReference fromServiceReference(Reference ref) {
		ScrReference scrReference = new ScrReference();
		scrReference.satisfied = ref.isSatisfied();
		scrReference.serviceInterface = ref.getServiceName();
		scrReference.targetFilter = ref.getTarget();
		scrReference.multiple = ref.isMultiple();
		scrReference.optional = ref.isOptional();
		scrReference.dynamic = !ref.isStatic();

		scrReference.boundServices = new LinkedList<BasicServiceInfo>();

		ServiceReference[] boundServiceReferences = ref
				.getBoundServiceReferences();
		if (boundServiceReferences != null) {
			for (ServiceReference serviceReference : boundServiceReferences) {
				BasicServiceInfo basicServiceInfo = BasicServiceInfo
						.fromServiceReference(serviceReference);

				scrReference.boundServices.add(basicServiceInfo);
			}
		}

		return scrReference;
	}

	public String getServiceInterface() {
		return serviceInterface;
	}

	public boolean isSatisfied() {
		return satisfied;
	}

	public String getTargetFilter() {
		return targetFilter;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public boolean isOptional() {
		return optional;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public List<BasicServiceInfo> getBoundServices() {
		return boundServices;
	}

}
