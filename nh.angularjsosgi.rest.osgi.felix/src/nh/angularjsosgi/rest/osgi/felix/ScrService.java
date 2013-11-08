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

/**
 * A Declarative Service
 * @author nils
 *
 */
public class ScrService {

	private  String type;
	private List<String> serviceInterfaces;

	
	public static ScrService fromComponent(Component component) {

		ScrService scrService = new ScrService();
				

		
		scrService.type = component.isServiceFactory() ? "service factory" : "service";
		scrService.serviceInterfaces = new LinkedList<String>();

		String[] services = component.getServices();
		if (services != null) {
			for (String serviceName : services) {
				scrService.addServiceInterface(serviceName);
			}
		}
		return scrService;

	}
	
	public String getType() {
		return type;
	}

	void addServiceInterface(String si) {
		this.serviceInterfaces.add(si);
	}

}
