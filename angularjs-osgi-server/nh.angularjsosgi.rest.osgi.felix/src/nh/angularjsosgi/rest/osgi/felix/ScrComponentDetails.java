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

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.Component;
import org.apache.felix.scr.Reference;

/**
 * Details of a Declarative Service Component
 * @author nils
 *
 */
public class ScrComponentDetails extends BasicScrComponent {

	private Map<String, String> properties;
	private String implementationClass;
	private String componentFactory;
	private String activation;
	private String defaultState;
	private String configurationPolicy;
	private String configurationPid;

	private ScrService service;

	private List<ScrReference> references;

	public static ScrComponentDetails fromScrComponent(Component component) {
		ScrComponentDetails details = new ScrComponentDetails();
		populateFromComponent(details, component);

		details.implementationClass = component.getClassName();
		details.componentFactory = component.getFactory();
		details.defaultState = component.isDefaultEnabled() ? "enabled"
				: "disabled";
		details.activation = component.isImmediate() ? "immediate" : "delayed";
		details.configurationPolicy = component.getConfigurationPolicy();
		details.configurationPid = component.getConfigurationPid();

		details.properties = new Hashtable<String, String>();
		Dictionary<String, Object> componentProperties = component
				.getProperties();
		Enumeration<String> propertyKeys = componentProperties.keys();

		while (propertyKeys.hasMoreElements()) {
			String propertyKey = propertyKeys.nextElement();

			details.properties.put(propertyKey,
					toString(componentProperties.get(propertyKey)));
		}

		details.service = ScrService.fromComponent(component);

		details.references = new LinkedList<ScrReference>();
		Reference[] references = component.getReferences();

		if (references != null) {
			for (Reference reference : references) {
				ScrReference scrReference = ScrReference
						.fromServiceReference(reference);
	
				details.references.add(scrReference);
			}
		}

		return details;
	}

	public String getImplementationClass() {
		return implementationClass;
	}

	public String getComponentFactory() {
		return componentFactory;
	}

	public String getActivation() {
		return activation;
	}

	public String getDefaultState() {
		return defaultState;
	}

	public String getConfigurationPid() {
		return configurationPid;
	}

	public String getConfigurationPolicy() {
		return configurationPolicy;
	}

	public ScrService getService() {
		return service;
	}

	public List<ScrReference> getReferences() {
		return references;
	}

}
