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
angular.module('OsgiMgmtApp.DsComponents')
	.filter('componentStateFilter', function() {
		return function(input,states) {
			var filtered = [];

			for (var componentId in input) {

				if (!input.hasOwnProperty(componentId)) {
					continue;
				}

				var candidate = input[componentId];

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

	.controller('DsComponentsController', function($scope, $http, $location, $routeParams, osgiMgmtUtils, OSGIMGMTAPP_DSCOMPONENTS_BASEDIR, API_URL) {
		/** Defined Standard DS States */
		var KNOWN_DS_STATES = [
			'Disabled',
			'Unsatisfied',
			'Active',
			'Registered',
			'Factory'
		];

		// =======================================================
		// ===   PAGE                                         ====
		// =======================================================
		var page = {
			'title': 'DS Components',
			'subtitle': 'Registered Declarative Service Components',
			'content': OSGIMGMTAPP_DSCOMPONENTS_BASEDIR + '/partials/list/page.html',
			'sidebar': OSGIMGMTAPP_DSCOMPONENTS_BASEDIR + '/partials/list/sidebar.html'
		};

		// =======================================================
		// ===   MODEL                                        ====
		// =======================================================
		var model = {
			components: {},
			states: {}
		};

		// =======================================================
		// ===   FUNCTIONS                                    ====
		// =======================================================
		addComponent = function(component) {

			if (!model.states.hasOwnProperty(component.state)) {
				// only in case a deprecated state is returned
				model.states[component.state] = {'state': component.state, 'count': 1, 'selected': true};
			}

			model.states[component.state]['count'] += 1;
			model.components[component.refId] = component;
		}

		resetComponentsAndStates = function() {
			model.components = {};
			model.states = {};

			for (var i = 0; i<KNOWN_DS_STATES.length;i++) {
				var state = {
					'state': KNOWN_DS_STATES[i],
					'selected': true,
					'count': 0
				}

				model.states[KNOWN_DS_STATES[i]] = state;
			}
		}

		readComponents = function() {

			// Make sure we're working on an empty model
			resetComponentsAndStates();

			$http.get(API_URL + 'dscomponents')
				.success(function(data) {
					model.service = data;
					console.log("RECEIVED DATA data: " + data);

					if (data.length) {
						page.subtitle = data.length + " Registered Declarative Service Components";
					} else {
						page.subtitle = "No Registered Declarative Service Components";
					}

					for (var i = 0;i<data.length;i++) {
						var component = data[i];

						console.log("Component.refIdd: " + component.refId);

						addComponent(component);
					}

				})
				.error(function(data, status, headers, config) {
					console.log(" ##### ERROR: " + status + ", data: " + data['message']);
					if (data.message) {
						model.errorMessage = data.message;
					} else {
						model.errorMessage = "Reading DS Components failed with HTTP Error " + status;
					}
				})
			;
		}

		/** Enables or disables the specified component */
		changeComponentState = function(component, action) {

			clearErrorMessage();

			var component = model.components[component.refId];

			var url = API_URL + 'dscomponents/' + encodeURIComponent(component.refId) + '/' + action;

			$http.put(url)
				.success(function(data) {
					console.log("  UPDATE COMPONENT: " + component.refId);

					var refId = data.refId;

					// re-read all components to reflect their (possibly) changed state
					readComponents();
				})
				.error(function(data, status, headers, config) {
					if (data.message) {
						model.errorMessage = data.message;
					} else {
						model.errorMessage = "Action failed with HTTP Error " + status;
					}
				});

		}

		enableComponent = function(component) {
			changeComponentState(component, 'enable');
		}

		disableComponent = function(component) {
			changeComponentState(component, 'disable');
		}

		showDetails = function(component) {
			var path = '/dscomponents/' + encodeURIComponent(component.refId);
			$location.path(path);
		}

		hasErrorMessage = function() {
			return model.errorMessage;
		}

		clearErrorMessage = function() {
			delete model.errorMessage;
		}

		getStateClass = function(state) {
			var candidate = state;

			return osgiMgmtUtils.getComponentStateClass(candidate);
		}

		toggleStateFilter = function(state) {

			var candidate = state;

			if (typeof state !== 'string') {
				// Passed in object should have a property 'state' containing the state as string
				candidate = state.state;
			}

			model.states[candidate].selected = ! model.states[candidate].selected;
			console.log("State '" + candidate + "': " + model.states[candidate]);
		}

		clearComponentFilter = function() {
			delete model.componentFilter;
		}

		// =======================================================
		// ===   SCOPE                                        ====
		// =======================================================
		$scope.page = page;
		$scope.model = model;
		$scope.hasErrorMessage = hasErrorMessage;
		$scope.clearErrorMessage = clearErrorMessage;
		$scope.getStateClass = getStateClass;
		$scope.toggleStateFilter = toggleStateFilter;
		$scope.clearComponentFilter = clearComponentFilter;
		$scope.showDetails = showDetails;
		$scope.enableComponent = enableComponent;
		$scope.disableComponent = disableComponent;

		// =======================================================
		// ===   INIT                                         ====
		// =======================================================
		readComponents();

	})
;