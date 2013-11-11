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

/**
 * @author Nils Hartmann <nils@nilshartmann.net>
 * 
 */
public class ModuleContribution {

	private String _name;
	private String _basedir;
	private String _scripts;

	/**
	 * @return the name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @return the basedir
	 */
	public String getBasedir() {
		return _basedir;
	}

	/**
	 * @return the scripts
	 */
	public String getScripts() {
		return _scripts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ModuleContribution [_name=" + _name + ", _basedir=" + _basedir
				+ ", _scripts=" + _scripts + "]";
	}

}
