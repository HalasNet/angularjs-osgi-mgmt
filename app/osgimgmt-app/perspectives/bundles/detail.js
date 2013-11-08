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

	.controller('BundlesDetailController', function($scope, $http, $timeout, $routeParams, $location, $window, OSGIMGMTAPP_BUNDLES_BASEDIR, API_URL) {

		// =======================================================
		// ===   PAGE                                         ====
		// =======================================================
		var page = {
			'title': 'Bundle Details ' + $routeParams.bundleId,
			'content': OSGIMGMTAPP_BUNDLES_BASEDIR + '/partials/detail/page.html',
			'sidebar': OSGIMGMTAPP_BUNDLES_BASEDIR + '/partials/detail/sidebar.html',
			'back': true
		};

		// =======================================================
		// ===   MODEL                                        ====
		// =======================================================
		var model = {
			bundleDetail: {}
		}

		// =======================================================
		// ===   FUNCTIONS                                    ====
		// =======================================================
		readBundleDetail = function() {
			$http.get(API_URL + 'bundles/' + $routeParams.bundleId + '/details').success(function(data) {
				model.bundleDetail = data;

				page.title = data.bundle.name;
				page.subtitle = data.bundle.symbolicName + " " + data.bundle.version + " [" + data.bundle.id + "]";
			});
		}

		showDetails = function(bundleId) {
			$location.path('/bundles/' + bundleId);
		}

		// =======================================================
		// ===   SCOPE                                        ====
		// =======================================================
		$scope.model = model;
		$scope.page = page;
		$scope.showDetails = showDetails;

		// =======================================================
		// ===   INIT                                         ====
		// =======================================================
		readBundleDetail();

	})
;
