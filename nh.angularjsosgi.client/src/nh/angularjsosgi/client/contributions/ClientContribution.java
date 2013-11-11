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

import java.net.URL;
import java.util.List;

import nh.angularjsosgi.http.Utils;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nils Hartmann <nils@nilshartmann.net>
 * 
 */
public class ClientContribution {
	private final Logger _logger = LoggerFactory.getLogger(getClass());

	private Bundle _providingBundle;
	private String _contributionPath;
	private List<ModuleContribution> modules;

	/**
	 * @return the moduleContributions
	 */
	public List<ModuleContribution> getModuleContributions() {
		return modules;
	}

	/**
	 * @param providingBundle
	 *            the providingBundle to set
	 */
	public void setProvidingBundle(Bundle providingBundle) {
		_providingBundle = providingBundle;
	}

	public URL getResource(String path) {
		String candidatePath = Utils.combine(_contributionPath, path);

		_logger.debug("Searching for '{}' in {}", candidatePath,
				_providingBundle);

		return _providingBundle.getEntry(candidatePath);
	}

	/**
	 * @param contributionPath
	 *            the contributionPath to set
	 */
	public void setContributionPath(String contributionPath) {
		_contributionPath = contributionPath;
	}

	/**
	 * @return the providingBundle
	 */
	public boolean isProvidedBy(Bundle bundle) {
		return bundle.equals(_providingBundle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClientContribution [moduleContributions=" + modules + "]";
	}

}
