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
angular.module('OsgiMgmtApp.Services')
	.controller('ServicesController', function($scope, $http, $timeout, $location, OSGIMGMTAPP_SERVICES_BASEDIR, API_URL) {
		// =======================================================
		// ===   PAGE                                         ====
		// =======================================================
		var page = {
			'title': 'Service Overview',
			'content': OSGIMGMTAPP_SERVICES_BASEDIR + '/partials/list/page.html',
			'sidebar': OSGIMGMTAPP_SERVICES_BASEDIR + '/partials/list/sidebar.html'

		};

		// =======================================================
		// ===   MODEL                                        ====
		// =======================================================
		var model = {
			services: [],
			serviceFilter: '',
			showUnusedServices: true
		};

		// =======================================================
		// ===   FUNCTIONS                                    ====
		// =======================================================
		readServices = function () {
			$http.get(API_URL + 'services').success(function(data) {

				if (data.length) {
					page.subtitle = data.length + " Services"
				}

				for (var i = 0;i<data.length;i++) {
					var serviceInfo = data[i];
					model.services.push(serviceInfo);
				}
			});
		};

		getTooltip = function(service) {
			if (!service) {
				return null;
			}

			var tooltip;

			if (service.description) {
				tooltip = service.description;
			}

			if (service.pid) {
				if (tooltip) {
					tooltip += " (PID: " + service.pid + ")";
				}
				tooltip = "PID: " + service.pid;
			}

			return tooltip;
		}

		toggleShowUnusedServices = function() {
			model.showUnusedServices = !model.showUnusedServices;
		}

		showDetails = function(serviceId) {
			console.log("Show Details for " + serviceId);
			$location.path('/services/' + serviceId);
		}

		// =======================================================
		// ===   SCOPE                                        ====
		// =======================================================
		$scope.page = page;
		$scope.model = model;
		$scope.getTooltip = getTooltip;
		$scope.toggleShowUnusedServices = toggleShowUnusedServices;
		$scope.showDetails = showDetails;

		// =======================================================
		// ===   INIT                                         ====
		// =======================================================
		readServices();

	})

	/** Filters out (un)used services */
	.filter('unusedServicesFilter', function() {

		return function(input,showUnusedServices) {
			if (showUnusedServices) {
				return input;
			}
			var filtered = [];

			for (var j = 0;j<input.length;j++) {
				var s = input[j];

				if (s.usingBundlesCount>0) {
					filtered.push(s);
				}
			}

			return filtered;
		}
	})
;