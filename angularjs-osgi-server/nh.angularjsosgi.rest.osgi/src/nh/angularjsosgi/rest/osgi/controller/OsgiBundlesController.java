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
import nh.angularjsosgi.controller.Request;
import nh.angularjsosgi.controller.RequestMapping;
import nh.angularjsosgi.controller.Response;
import nh.angularjsosgi.rest.osgi.model.BundleDetails;
import nh.angularjsosgi.rest.osgi.model.BundleInfo;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Component;

/**
 * Controller that handles requests for bundle informations
 * 
 * @author nils
 * 
 */
@Component(provide = Controller.class)
public class OsgiBundlesController extends AbstractOsgiController {

	private BundleContext bundleContext;

	/**
	 * Returns a list of bundles.
	 * 
	 * This methode either returns a list of all bundles or bundles specified
	 * with <tt>bid</tt> parameter, that can take a comma-separated list of
	 * bundle ids. In case a bundle is not found it is simply ignored (as
	 * opposite to {@link #getBundle(Request, Response, long)} that returns a
	 * 404 in that case
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("GET /bundles")
	public void getBundles(Request request, Response response) throws Exception {

		List<BundleInfo> bundleInfos = new LinkedList<BundleInfo>();

		String query = request.getQueryParameter("bid");

		_logger.debug("query: " + query);

		if (query != null) {

			String[] ids = query.split(",");

			for (String id : ids) {
				long bundleId = asLong(id);

				Bundle bundle = bundleContext.getBundle(bundleId);

				if (bundle != null) {
					BundleInfo bundleInfo = BundleInfo.fromBundle(bundle);
					bundleInfos.add(bundleInfo);
				}
			}

		} else {

			Bundle[] bundles = bundleContext.getBundles();

			for (Bundle bundle : bundles) {
				BundleInfo bundleInfo = BundleInfo.fromBundle(bundle);

				bundleInfos.add(bundleInfo);
			}
		}

		response.json(bundleInfos.toArray());

	}

	/**
	 * Returns informations about the specified bundle. Throws a notFound error
	 * in case the specified bundle does not exists
	 * 
	 * @param request
	 * @param response
	 * @param bundleId
	 * @throws Exception
	 */
	@RequestMapping("GET bundles/{bundleId}")
	public void getBundle(Request request, Response response, long bundleId)
			throws Exception {

		Bundle bundle = getExistingBundle(bundleId);

		BundleInfo bundleInfo = BundleInfo.fromBundle(bundle);

		response.json(bundleInfo);
	}

	/**
	 * 
	 * @param response
	 * @param bundleId
	 * @param action
	 */
	@RequestMapping("OPTIONS /bundles/{bundleId}/{action}")
	public void optionsBundleDetails(Response response, long bundleId,
			String action) {
		_logger.info("OPTIONS for " + action);

		response.send(OK_NO_CONTENT);
	}

	/**
	 * Returns details of the specified bundles. Returns notFound in case the
	 * requests bundle is not available
	 * 
	 * @param response
	 * @param bundleId
	 */
	@RequestMapping("GET /bundles/{bundleId}/details")
	public void getBundleDetails(Response response, long bundleId) {
		Bundle bundle = getExistingBundle(bundleId);

		BundleDetails bundleDetails = BundleDetails.fromBundle(bundle);

		response.json(bundleDetails);
	}

	/**
	 * Starts the specified bundle.
	 * 
	 * <p>
	 * Returns a <tt>notFound</tt> error in case the requested bundle does not
	 * exist
	 * <p>
	 * Returns a <tt>badRequest</tt> error in case the requested bundle is in
	 * wrong state and the operation cannot be executed
	 * 
	 * <p>
	 * Return <tt>okNotContent</tt> on successful operation.
	 * 
	 * @param response
	 * @param bundleId
	 */
	@RequestMapping("PUT /bundles/{bundleId}/start")
	public void startBundle(Response response, long bundleId) {
		_logger.info("Starting Bundle " + bundleId);
		Bundle bundle = getExistingBundle(bundleId);

		if (bundle.getState() == Bundle.UNINSTALLED) {
			throw badRequest("Bundle already uninstalled");
		}

		try {
			bundle.start();
		} catch (BundleException e) {
			_logger.error("Unable to Start bundle: " + e, e);
			throw badRequest("Unable to start bundle: " + e);
		}

		response.send(OK_NO_CONTENT);
	}

	/**
	 * Stops the specified bundle.
	 * 
	 * <p>
	 * Returns a <tt>notFound</tt> error in case the requested bundle does not
	 * exist
	 * <p>
	 * Returns a <tt>badRequest</tt> error in case the requested bundle is in
	 * wrong state and the operation cannot be executed
	 * 
	 * <p>
	 * Return <tt>okNotContent</tt> on successful operation.
	 * 
	 * @param response
	 * @param bundleId
	 */
	@RequestMapping("PUT /bundles/{bundleId}/stop")
	public void stopBundle(Response response, long bundleId) {
		Bundle bundle = getExistingBundle(bundleId);

		if (bundle.getState() == Bundle.UNINSTALLED) {
			throw badRequest("Bundle already uninstalled");
		}

		try {
			bundle.stop();
		} catch (BundleException e) {
			_logger.error("Unable to Stop bundle: " + e, e);
			throw badRequest("Unable to stop bundle: " + e);
		}

		response.send(OK_NO_CONTENT);
	}

	/**
	 * Unintalls the specified bundle.
	 * 
	 * <p>
	 * Returns a <tt>notFound</tt> error in case the requested bundle does not
	 * exist
	 * <p>
	 * Returns a <tt>badRequest</tt> error in case the requested bundle is in
	 * wrong state and the operation cannot be executed
	 * 
	 * <p>
	 * Return <tt>okNotContent</tt> on successful operation.
	 * 
	 * @param response
	 * @param bundleId
	 */
	@RequestMapping("DELETE /bundles/{bundleId}")
	public void uninstallBundle(Response response, long bundleId) {
		Bundle bundle = getExistingBundle(bundleId);

		if (bundle.getState() == Bundle.UNINSTALLED) {
			throw badRequest("Bundle already uninstalled");
		}

		try {
			bundle.uninstall();
		} catch (BundleException e) {
			_logger.error("Unable to Stop bundle: " + e, e);
			throw badRequest("Unable to stop bundle: " + e);
		}

		response.send(OK_NO_CONTENT);
	}

	protected Bundle getExistingBundle(long bundleId) {
		Bundle bundle = bundleContext.getBundle(bundleId);
		if (bundle == null) {
			_logger.error("No Bundle found with id " + bundleId);
			throw notFound();

		}
		return bundle;

	}

	public void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
	}

}
