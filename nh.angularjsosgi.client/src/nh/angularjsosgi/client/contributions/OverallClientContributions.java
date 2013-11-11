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

package nh.angularjsosgi.client.contributions;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Nils Hartmann <nils@nilshartmann.net>
 * 
 */
public class OverallClientContributions {

	private List<ModuleContribution> _modules = new CopyOnWriteArrayList<>();

	public void addClientContribution(ClientContribution clientContribution) {
		_modules.addAll(clientContribution.getModuleContributions());
	}

	public void removeClientContribution(ClientContribution clientContribution) {
		_modules.removeAll(clientContribution.getModuleContributions());
	}

}
