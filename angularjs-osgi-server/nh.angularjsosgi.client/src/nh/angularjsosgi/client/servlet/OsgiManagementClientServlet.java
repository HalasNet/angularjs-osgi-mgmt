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

package nh.angularjsosgi.client.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nh.angularjsosgi.client.contributions.ClientContributionBundleTracker;
import nh.angularjsosgi.client.contributions.OverallClientContributions;
import nh.angularjsosgi.http.Utils;
import nh.angularjsosgi.util.json.JsonService;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

/**
 * @author Nils Hartmann <nils@nilshartmann.net>
 * 
 */
@Component(immediate = true)
public class OsgiManagementClientServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Servlet Context Path */
	private final static String CONTEXT_PATH = "/";

	/** Folder in this bundle containing the webapp resources */
	public final static String WEBAPP_FOLDER = "/webapp";

	private final Logger _logger = LoggerFactory.getLogger(getClass());

	private BundleContext _bundleContext;

	private ClientContributionBundleTracker _clientExtensionBundleTracker;

	private JsonService _jsonService;

	@Activate
	public void activate(ComponentContext componentContext) {
		_logger.info("Activate " + getClass().getSimpleName());

		_bundleContext = componentContext.getBundleContext();

		_clientExtensionBundleTracker = new ClientContributionBundleTracker(
				componentContext.getBundleContext(), _jsonService);
		_clientExtensionBundleTracker.open();
	}

	public void deactivate() {
		_logger.info("Deactivate " + getClass().getSimpleName());
		if (_clientExtensionBundleTracker != null) {
			_clientExtensionBundleTracker.close();
		}
	}

	/**
	 * @param jsonService
	 *            the jsonService to set
	 */
	@Reference(dynamic = false, multiple = false, optional = false)
	public void setJsonService(JsonService jsonService) {
		_jsonService = jsonService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String pathInfo = Utils.normalize(req.getPathInfo());
		_logger.info("Path invoked '{}'", pathInfo);

		if ("/client-config.json".equals(pathInfo)) {
			_logger.info("Client Configuration requested");
			OverallClientContributions overallClientContributions = _clientExtensionBundleTracker
					.getOverallClientContributions();
			_jsonService.toJson(resp.getWriter(), overallClientContributions);
			return;
		}

		if (pathInfo == null || "/".equals(pathInfo)) {
			pathInfo = "/index.html";
		}

		// check if one of our own resources is requests
		String candidatePath = WEBAPP_FOLDER + pathInfo;

		URL entry = _bundleContext.getBundle().getEntry(candidatePath);
		if (entry != null) {
			_logger.debug("Resource found: " + entry);

			returnResource(resp, pathInfo, entry);
			return;
		}

		// try to find resource from contributions
		entry = _clientExtensionBundleTracker.getResource(pathInfo);

		if (entry != null) {
			_logger.debug("Resource found: " + entry);

			returnResource(resp, pathInfo, entry);
			return;

		}

		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	protected void returnResource(HttpServletResponse response,
			String pathInfo, URL url) throws IOException {
		URLConnection connection = url.openConnection();
		try (InputStream ins = connection.getInputStream()) {
			response.setContentType(getServletContext().getMimeType(pathInfo));
			response.setIntHeader("Content-Length",
					connection.getContentLength());

			// spool the actual contents
			OutputStream out = response.getOutputStream();
			byte[] buf = new byte[2048];
			int rd;
			while ((rd = ins.read(buf)) >= 0) {
				out.write(buf, 0, rd);
			}
		}

	}

	// =====================================================================
	// === SERVICE BINDING
	// =====================================================================
	@Reference(dynamic = true, multiple = true)
	public void bindHttpService(HttpService httpService)
			throws ServletException, NamespaceException {

		_logger.info("binding http service: " + httpService);
		httpService.registerServlet(CONTEXT_PATH, this, null, null);
	}

	public void unbindHttpService(HttpService httpService)
			throws ServletException, NamespaceException {
		_logger.info("unbind http service: " + httpService);
		try {
			httpService.unregister(CONTEXT_PATH);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
