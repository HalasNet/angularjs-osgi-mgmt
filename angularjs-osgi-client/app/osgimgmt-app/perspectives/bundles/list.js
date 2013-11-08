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
angular.module('OsgiMgmtApp.Bundles')

	/** A filter that filters a list of bundles according to their states */
	.filter('bundleState', function() {
		return function(input,states) {
			if (Object.getOwnPropertyNames(states).length === 0) {
				// no states defined
				console.log(" Return true, as no filter has been defined");
				return input;
			}

			var filtered = [];

			for (var bundleId in input) {

				if (!input.hasOwnProperty(bundleId)) {
					continue;
				}

				var candidate = input[bundleId];

				var actualState = candidate.state;

				if (!states.hasOwnProperty(actualState)) {
					// no such state in state configuration
					filtered.push(candidate);
				} else if (states[actualState].selected === true) {
					filtered.push(candidate);
				}
			}

			return filtered;
		}
	})

	.controller('BundlesController', function($scope, $http, $timeout, $location, osgiMgmtUtils, OSGIMGMTAPP_BUNDLES_BASEDIR, API_URL) {

		// =======================================================
		// ===   PAGE                                         ====
		// =======================================================
		var page = {
			'title': 'Bundle Overview',
			'content': OSGIMGMTAPP_BUNDLES_BASEDIR + '/partials/list/page.html',
			'sidebar': OSGIMGMTAPP_BUNDLES_BASEDIR + '/partials/list/sidebar.html'
		};


		// =======================================================
		// ===   MODEL                                        ====
		// =======================================================
		var model = {
			/** All Bundles */
			bundleInfos: {},

			/** Bundle Filter */
			'bundleFilter': '',

			/** Bundle State Statistics */
			states: {
				'Installed': {
					state: 'Installed',
					count: 0,
					selected: true
				},

				'Resolved': {
					state: 'Resolved',
					count: 0,
					selected: true
				},

				'Starting': {
					state: 'Starting',
					count: 0,
					selected: true
				},

				'Active': {
					state: 'Active',
					count: 0,
					selected: true
				},

				'Stopping': {
					state: 'Stopping',
					count: 0,
					selected: true
				}
			},

			/** Error message (if any) to be displayed in the error message box at the page's top */
			'lastError': ''

		}

		// =======================================================
		// ===   FUNCTIONS                                    ====
		// =======================================================
		function resetBundlesAndStates() {
			model.bundleInfos = {};

			$.each(model.states,function(key, state) {
				state.count = 0;
			});
		}

		function readBundleInfos() {
			// Clean model
			resetBundlesAndStates();

			// Read Bundles
			$http.get(API_URL + 'bundles')
				.success(function(data) {

					if (data.length) {
						page.subtitle = data.length + " Bundles found";
					} else {
						page.substitle = "No Bundle Information available";
					}

					for (var i = 0;i<data.length;i++) {
						var bundleInfo = data[i];

						setBundleInfo(bundleInfo);
					}
				})
				.error(function(data, status, headers, config) {
					if (data.message) {
						model.lastError = data.message;
					} else {
						model.lastError = "Reading DS Components failed with HTTP Error " + status;
					}

				})
			;
		};

		function updateBundleInfo(bundleId) {
			$http.get(API_URL + 'bundles?bid=' + bundleId).success(function(data) {

				if (data.length==0) {
					delete model.bundleInfos[bundleId];
				} else {
					var bundleData = data[0];
					setBundleInfo(bundleData);
				}
			});
		}

		function setBundleInfo(bundleInfo) {
			if (!model.states.hasOwnProperty(bundleInfo.state)) {
				model.states[bundleInfo.state] = {'state': bundleInfo.state, 'count': 1, 'selected': true};
			} else {
				model.states[bundleInfo.state]['count'] += 1;
			}

			model.bundleInfos[bundleInfo.id] = bundleInfo;

		}

		function executeBundleAction(bundleId, httpCall) {

			model.lastError = '';

			var bundle = model.bundleInfos[bundleId];

			httpCall()
				.success(function() {
					readBundleInfos();
				})
				.error(function(data, status, headers, config) {
					if (data.message) {
						model.lastError = data.message;
					} else {
						model.lastError = "Action failed with HTTP Error " + status;
					}
			});
		}

		// =======================================================
		// ===   SCOPE                                        ====
		// =======================================================
		$scope.model = model;
		$scope.page = page;

		$scope.showDetails = function(bundleId) {
			$location.path('/bundles/' + bundleId);
		}

		$scope.toggleStateFilter = function(state) {

			var candidate = state;

			if (typeof state !== 'string') {
				// Passed in object should have a property 'state' containing the (bundle) state as string
				candidate = state.state;
			}

			model.states[candidate].selected = ! model.states[candidate].selected;
			console.log("State '" + candidate + "': " + model.states[candidate]);
		}

		$scope.clearBundleFilter = function() {
			model.bundleFilter = '';
		}

		$scope.hasErrorMessage = function() {
			return model.lastError==='';
		}

		$scope.dismissErrorMessage = function() {
			model.lastError = '';
		}

		$scope.getStateClass = function(state) {
			return osgiMgmtUtils.getBundleStateClass(state);
		}

		$scope.stopBundle = function(bundleId) {
			executeBundleAction(bundleId, function() {
				return $http.put(API_URL + 'bundles/' + bundleId + '/stop');
			});
		};

		$scope.startBundle = function(bundleId) {
			executeBundleAction(bundleId, function() {
				return $http.put(API_URL + 'bundles/' + bundleId + '/start');
			});

		};

		$scope.uninstallBundle = function(bundleId) {
			$http.delete(API_URL + 'bundles/' + bundleId).success(function() {
				updateBundleInfo(bundleId);
			});
		};

		// =======================================================
		// ===   INIT                                         ====
		// =======================================================
		readBundleInfos();
	})
;