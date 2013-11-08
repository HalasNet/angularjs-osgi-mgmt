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

import org.apache.felix.scr.Component;
import org.apache.felix.scr.ScrService;
import org.osgi.framework.Constants;

import aQute.bnd.annotation.component.Reference;
import nh.angularjsosgi.controller.Controller;
import nh.angularjsosgi.controller.Request;
import nh.angularjsosgi.controller.RequestMapping;
import nh.angularjsosgi.controller.Response;
import nh.angularjsosgi.rest.osgi.controller.AbstractOsgiController;

/**
 * A Controller that handles requests for Declarative Service Components
 * @author nils
 *
 */
@aQute.bnd.annotation.component.Component(provide = Controller.class)
public class DsComponentsController extends AbstractOsgiController {

	private ScrService scrService;

	@RequestMapping("GET /dscomponents")
	public void getDsComponents(Request request, Response response) {

		Component[] components;

		String queryCid = request.getQueryParameter("cid");

		if (queryCid != null) {
			
			Component component = findComponent(queryCid);
			
			if (component != null) {
				components = new Component[] { component };
			} else {
				components = new Component[] {};
			}
		} else {
			// No query specified, return all components

			components = scrService.getComponents();
		}

		List<BasicScrComponent> scrComponents = new LinkedList<BasicScrComponent>();

		for (Component component : components) {
			BasicScrComponent basicScrComponent = BasicScrComponent
					.fromComponent(component);

			scrComponents.add(basicScrComponent);
		}

		response.json(scrComponents);
	}

	@RequestMapping("GET /dscomponents/{componentRefId}/details")
	public void getDsComponentDetails(Response response, String componentRefId)
			throws Exception {
		Component component = getExistingComponent(componentRefId);

		ScrComponentDetails details = ScrComponentDetails
				.fromScrComponent(component);

		response.json(details);
	}

	@RequestMapping("OPTIONS /dscomponents/{componentRefId}/{action}")
	public void optionsComponnentDetails(Response response,
			String componentsRefId, String action) {
		_logger.info("OPTIONS for " + action);

		response.send(OK_NO_CONTENT);
	}

	@RequestMapping("PUT /dscomponents/{componentRefId}/enable")
	public void enableComponent(Response response, String componentRefId)
			throws Exception {
		Component component = getExistingComponent(componentRefId);

		_logger.info("Enabling Component " + componentRefId);

		component.enable();

		response.json(keyValue("refId", BasicScrComponent.getRefId(component)));
	}

	@RequestMapping("PUT /dscomponents/{componentRefId}/disable")
	public void disableComponent(Response response, String componentRefId)
			throws Exception {
		Component component = getExistingComponent(componentRefId);

		_logger.info("Disabling Component " + componentRefId);

		component.disable();

		response.json(keyValue("refId", BasicScrComponent.getRefId(component)));
		
	}
	/**
	 * Returns the Component with the specified componentId. Throws notFound in
	 * case the component does not exists
	 * 
	 * @param componentId
	 * @return Component. Never null.
	 */
	protected Component getExistingComponent(String componentRefId) {
		
		Component component = findComponent(componentRefId);
		
		if (component == null) {
				throw entityNotFound("DS Component", componentRefId);
		}

		return component;
	}
	
	protected Component findComponent(String componentRefId) {
		
		
		if (componentRefId == null || componentRefId.trim().isEmpty()) {
			throw badRequest("Parameter 'componentRefId' must be set");
		}
		
		_logger.debug("Trying to find DS Component '" + componentRefId + "'");
		
		Component component = null;
		
		try {
			long componentId = Long.parseLong(componentRefId);
			component = scrService.getComponent(componentId);
		} catch (NumberFormatException e) {
			_logger.debug("Component Id '" + componentRefId + "' could not be parsed to long. Try using as component name");
		}
		
		if (component == null) {
			component = findComponentByName(componentRefId);
		}
		
		
		return component;
	}

	protected Component findComponentByName(String componentRefId) {
		// taken from org.apache.felix.webconsole.internal.compendium.ComponentsServlet.RequestInfo.getComponentByName(String)
		final int slash = componentRefId.lastIndexOf('/');
		final String componentName;
		final String pid;
		if (slash > 0) {
			componentName = componentRefId.substring(0, slash);
			pid = componentRefId.substring(slash + 1);
		} else {
			componentName = componentRefId;
			pid = null;
		}

		Component[] components;
		try {
			components = scrService.getComponents(componentName);
		} catch (Throwable t) {
			// not implemented in the used API version
			components = null;
		}

		if (components != null) {
			if (pid != null) {
				for (int i = 0; i < components.length; i++) {
					Component component = components[i];
					if (pid.equals(component.getProperties().get(
							Constants.SERVICE_PID))) {
						return component;
					}
				}
			} else if (components.length > 0) {
				return components[0];
			}
		}
		
		return null;
	}

	/**
	 * 
	 * @param scrService
	 */
	@Reference
	public void setScrService(ScrService scrService) {
		this.scrService = scrService;
	}

}
