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
angular.module('Platform.PerspectiveManager', ['ngRoute'])

	/** Route for pages not found */
	.config(function ($routeProvider) {
		// default route to notfound page
		$routeProvider.otherwise({ templateUrl: '/platform/perspectivemanager/templates/notfound.html' });
	})

	.provider('perspectiveRegistry', ['$routeProvider', function($routeProvider) {
		console.log("PERSPECTIVE REGISTRY");

		/** All known perspectives */
		this.perspectives = [];

		/** Registers a new perspective with the specified path, title and controller and - optional - aliases
		 *
		 * @param path the base path for the perspective
		 * @param title Title of the perspective that is displayed in the navbar
		 * @param controller Controller class of the perspective
		 * @param (optional) aliases: aliases that can be used to access the perspective
		 **/
		this.addPerspective = function(path, title, controller, aliases) {

			// add perspective
			this.perspectives.push({'path':path, 'title': title});

			// configure (root) route to perspective
			this.configurePerspectiveRoute(path, controller);

			if (aliases) {
				if (angular.isArray(aliases)) {
					for (var i=0;i<aliases.length;i++) {
						this.addRedirect(aliases[i], '/' + path);
					}
				} else {
					this.addRedirect(aliases, '/' + path);
				}
			}

			return this;

		};

		/** adds a route to the route provider */
		this.addRoute = function(path, controller) {
			$routeProvider.when(path,
				{	controller: controller,
					templateUrl: '/platform/perspectivemanager/templates/layout-partial.html'
				});

			return this;
		}

		this.addRedirect = function(alias, to) {
			console.log("  Create Alias from '" + alias + "' to '" + to + "'");
			$routeProvider.when(alias, {'redirectTo': to});
		};

		this.configurePerspectiveRoute = function(path, controller) {
			$routeProvider.when('/' + path,
				{	controller: controller,
					templateUrl: '/platform/perspectivemanager/templates//layout-partial.html'
				});
		}

		/** PUBLIC API */
		this.$get = function() {
			var _perspectives = this.perspectives;

			return {
				/** Returns a list of all known perspectives */
				getRegisteredPerspectives : function() {
					return _perspectives;
				}

			};
		};
	}])
;

