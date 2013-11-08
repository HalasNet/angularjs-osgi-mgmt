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

import org.apache.felix.scr.Component;
import org.osgi.framework.Constants;

import nh.angularjsosgi.rest.osgi.model.AbstractRestObject;
import nh.angularjsosgi.rest.osgi.model.InstalledBundleId;

/**
 * Some basic informations about a DS Component
 * @author nils
 *
 */
public class BasicScrComponent extends AbstractRestObject {

	private long id;

	/**
	 * "Id" that allows identification of a component even if it has no id
	 * (disabled component)
	 */
	private String refId;

	private String name;
	private InstalledBundleId declaringBundle;
	private String state;

	public static BasicScrComponent fromComponent(Component component) {
		BasicScrComponent basicScrComponent = new BasicScrComponent();
		populateFromComponent(basicScrComponent, component);

		return basicScrComponent;
	}

	protected static void populateFromComponent(
			BasicScrComponent basicScrComponent, Component component) {

		basicScrComponent.name = component.getName();
		basicScrComponent.id = component.getId();
		basicScrComponent.state = toStateString(component.getState());
		basicScrComponent.refId = getRefId(component);
		basicScrComponent.declaringBundle = InstalledBundleId
				.fromBundle(component.getBundle());

	}

	public static String getRefId(Component component) {
		String refId = null;

		if (component.getId() < 0) {
			// disabled component
			String k = component.getName();
			String pid = (String) component.getProperties().get(
					Constants.SERVICE_PID);
			if (pid != null) {
				k += '/' + pid;
			}
			refId = k;
		} else {
			refId = String.valueOf(component.getId());
		}

		return refId;

	}

	public long getId() {
		return id;
	}

	public String getRefId() {
		return refId;
	}

	public String getName() {
		return name;
	}

	public InstalledBundleId getDeclaringBundle() {
		return declaringBundle;
	}

	public String getState() {
		return state;
	}

	static String toStateString(int state) {
		switch (state) {
		case Component.STATE_DISABLED:
			return "Disabled";
		case Component.STATE_ENABLED:
			return "Enabled";
		case Component.STATE_UNSATISFIED:
			return "Unsatisfied";
		case Component.STATE_ACTIVATING:
			return "Activating";
		case Component.STATE_ACTIVE:
			return "Active";
		case Component.STATE_REGISTERED:
			return "Registered";
		case Component.STATE_FACTORY:
			return "Factory";
		case Component.STATE_DEACTIVATING:
			return "Deactivating";
		case Component.STATE_DESTROYED:
			return "Destroyed";
		default:
			return String.valueOf(state);
		}
	}

}
