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

import java.util.Map;

/**
 * An OSGi Capability provided by a bundle
 * 
 * @author nils
 * 
 */
public class Capability extends AbstractRestObject {

	protected Map<String, String> directives;

	protected Map<String, String> attributes;

	public Capability() {
	}

}
