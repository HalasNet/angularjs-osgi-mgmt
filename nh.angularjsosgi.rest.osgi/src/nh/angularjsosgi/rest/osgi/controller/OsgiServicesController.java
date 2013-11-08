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
package nh.angularjsosgi.rest.osgi.controller;

import java.util.LinkedList;
import java.util.List;

import nh.angularjsosgi.controller.Controller;
import nh.angularjsosgi.controller.RequestMapping;
import nh.angularjsosgi.controller.Response;
import nh.angularjsosgi.rest.osgi.model.BasicServiceInfo;
import nh.angularjsosgi.rest.osgi.model.ServiceDetails;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;

/**
 * Controller that handles requests for OSGi Service informations
 * 
 * @author nils
 * 
 */
@Component(provide = Controller.class)
public class OsgiServicesController extends AbstractOsgiController {

	private BundleContext bundleContext;

	@RequestMapping("GET /services")
	public void getAllServices(Response response) throws InvalidSyntaxException {

		ServiceReference<?>[] allServiceReferences = bundleContext
				.getAllServiceReferences(null, null);

		List<BasicServiceInfo> result = new LinkedList<BasicServiceInfo>();

		for (ServiceReference<?> sr : allServiceReferences) {
			BasicServiceInfo info = BasicServiceInfo.fromServiceReference(sr);

			result.add(info);
		}

		response.json(result);

	}

	@RequestMapping("GET /services/{serviceId}")
	public void getService(Response response, long serviceId) throws Exception {

		ServiceReference<?> existingServiceRef = getExistingServiceRef(serviceId);

		BasicServiceInfo serviceInfo = BasicServiceInfo
				.fromServiceReference(existingServiceRef);

		response.json(serviceInfo);
	}

	@RequestMapping("GET /services/{serviceId}/details")
	public void getServiceDetails(Response response, long serviceId)
			throws Exception {

		ServiceReference<?> existingServiceRef = getExistingServiceRef(serviceId);

		ServiceDetails serviceDetails = ServiceDetails
				.fromServiceReference(existingServiceRef);

		response.json(serviceDetails);
	}

	protected ServiceReference<?> getExistingServiceRef(long id)
			throws Exception {
		ServiceReference<?>[] serviceReferences = bundleContext
				.getAllServiceReferences(null, "(" + Constants.SERVICE_ID + "="
						+ id + ")");

		if (serviceReferences == null) {
			_logger.error("No Service with Id '" + id + "' found");
			throw notFound();
		}

		return serviceReferences[0];
	}

	@Activate
	public void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
	}

}
