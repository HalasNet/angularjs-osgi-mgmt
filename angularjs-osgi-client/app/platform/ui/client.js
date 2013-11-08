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
angular.module('Platform.Ui.Client',[])
	/** Controller for the Navigation Bar */
	.controller('NavBarController', function ($scope, $location, perspectiveRegistry) {
		$scope.parts = perspectiveRegistry.getRegisteredPerspectives();

		$scope.isActive = function (path) {
			if ($location.path().substr(0, path.length) == path) {
				return true
			} else {
				return false;
			}
		}
	})

	/** Provides common functionality for the whole App. This controller is the top-most parent controller */
	.controller('AppController', function($scope, $window) {

		/** Executes the window's history back function */
		$scope.$back = function() {
			$window.history.back();
		}

		/** Returns the number of 'own' properties of the specified object */
		$scope.getPropertyCount = function(o) {

			if (!o) {
				return 0;
			}

			var pc = 0;

			for (var p in o) {
				if (o.hasOwnProperty(p)) {
					pc += 1;
				}
			}

			return pc;
		}

	})

;
