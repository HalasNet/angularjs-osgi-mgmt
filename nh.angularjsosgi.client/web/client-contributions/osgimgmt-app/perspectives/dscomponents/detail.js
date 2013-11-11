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
	.filter('arraySort', function() {

		return function(input,direction) {

			if (!angular.isDefined(input)) {
				return input;
			}

			if (!direction || direction.charAt(0) != '-') {
				return input.sort();
			}

			return input.sort(function (a, b) {
				if (a > b)
					return -1;
				if (a < b)
					return 1;
				// a must be equal to b
				return 0;
			});

		}
	})
	.controller('DsComponentDetailController', function($scope, $http, $location, $routeParams, OSGIMGMTAPP_DSCOMPONENTS_BASEDIR, API_URL) {

		// =======================================================
		// ===   PAGE                                         ====
		// =======================================================
		var page = {
			'title': 'Component Details',
			'content': OSGIMGMTAPP_DSCOMPONENTS_BASEDIR + '/partials/detail/page.html',
			'sidebar': OSGIMGMTAPP_DSCOMPONENTS_BASEDIR + '/partials/detail/sidebar.html',
			'back': true
		};

		// =======================================================
		// ===   MODEL                                        ====
		// =======================================================
		var model = {

		};

		// =======================================================
		// ===   FUNCTIONS                                    ====
		// =======================================================
		readComponent = function() {
			$http.get(API_URL + 'dscomponents/' + $routeParams.componentId + '/details')
				.success(function(data) {
					model.component = data;
					console.log("RECEIVED DATA data: " + data);

					if (data.name) {
						page.subtitle = data.name;
					} else {
						page.subtitle = data.id;
					}
				})
				.error(function(data, status, headers, config) {
					console.log(" ##### ERROR: " + status + ", data: " + data['message']);
					if (data.message) {
						model.errorMessage = data.message;
					} else {
						model.errorMessage = "Action failed with HTTP Error " + status;
					}
				})
			;
		}

		hasErrorMessage = function() {
			return model.errorMessage;
		}

		clearErrorMessage = function() {
			delete model.errorMessage;
		}

		// =======================================================
		// ===   SCOPE                                        ====
		// =======================================================
		$scope.page = page;
		$scope.model = model;
		$scope.hasErrorMessage = hasErrorMessage;
		$scope.clearErrorMessage = clearErrorMessage;

		// =======================================================
		// ===   INIT                                         ====
		// =======================================================
		readComponent();

	})
;