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
import java.util.concurrent.CopyOnWriteArrayList;

import nh.angularjsosgi.util.json.JsonService;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nils Hartmann <nils@nilshartmann.net>
 * 
 */
public class ClientContributionBundleTracker extends
		BundleTracker<ClientContribution> {
	private final Logger _logger = LoggerFactory.getLogger(getClass());

	/** Name of the Header that points to a contribution */
	public final static String CLIENT_CONTRIBUTION_DIRECTORY = "OsgiMgmt-Client-Contribution-Dir";

	/** Name of the contribution definition file */
	public final static String CLIENT_CONTRIBUTION_CONFIG_FILE = "client-contribution.json";

	private JsonService _jsonService;

	private final List<ClientContribution> _clientContributions = new CopyOnWriteArrayList<ClientContribution>();

	/**
	 * Json Object for client-config.json
	 */
	private final OverallClientContributions _overallClientContributions;

	public ClientContributionBundleTracker(BundleContext context,
			JsonService jsonService) {
		super(context, Bundle.ACTIVE, null);
		_overallClientContributions = new OverallClientContributions();
		_jsonService = jsonService;
	}

	@Override
	public ClientContribution addingBundle(Bundle bundle, BundleEvent event) {

		String clientContributionDirectory = bundle.getHeaders().get(
				CLIENT_CONTRIBUTION_DIRECTORY);

		if (clientContributionDirectory == null) {
			return null;
		}

		_logger.debug("Found Module Contribution {} in bundle {}",
				clientContributionDirectory, bundle.getSymbolicName());

		String configPath = clientContributionDirectory + "/"
				+ CLIENT_CONTRIBUTION_CONFIG_FILE;
		URL configEntry = bundle.getEntry(configPath);

		if (configEntry == null) {
			_logger.error("No Config Path '{}' found in bundle {}", configPath,
					bundle.getSymbolicName());

			return null;
		}
		ClientContribution clientContribution = readClientContribution(bundle,
				configEntry);
		if (clientContribution != null) {
			clientContribution.setProvidingBundle(bundle);
			clientContribution.setContributionPath(clientContributionDirectory);
			_clientContributions.add(clientContribution);
			_overallClientContributions
					.addClientContribution(clientContribution);
		}

		return clientContribution;

	}

	@Override
	public void removedBundle(Bundle bundle, BundleEvent event,
			ClientContribution clientContribution) {
		if (clientContribution == null) {
			return;
		}

		//
		_logger.info("Removing Client Contribution from bundle {}",
				bundle.getSymbolicName());

		_clientContributions.remove(clientContribution);
		_overallClientContributions
				.removeClientContribution(clientContribution);
	}

	protected ClientContribution readClientContribution(Bundle bundle,
			URL configEntry) {
		try {
			ClientContribution clientContribution = _jsonService.fromJson(
					configEntry.openStream(), ClientContribution.class);

			_logger.debug("Read Client Contribution: " + clientContribution);

			return clientContribution;
		} catch (Exception ex) {
			_logger.error("Could not read Client Contribution from: "
					+ configEntry + ": " + ex, ex);
		}

		return null;

	}

	/**
	 * @return the overallClientContributions
	 */
	public OverallClientContributions getOverallClientContributions() {
		return _overallClientContributions;
	}

	/**
	 * Try to find the requested path in any of the contributed modules
	 * 
	 * @param pathInfo
	 * @return the resource's URL if found or null
	 */
	public URL getResource(String pathInfo) {

		// TODO: faster lookup; Cache etc
		for (ClientContribution clientContribution : _clientContributions) {

			URL url = clientContribution.getResource(pathInfo);

			if (url != null) {
				return url;
			}
		}

		return null;
	}
}
