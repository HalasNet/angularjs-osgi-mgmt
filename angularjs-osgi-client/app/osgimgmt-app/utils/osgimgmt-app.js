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


/** ============================================================================
 *  ==     The OSGi Management Application                                    ==
 *  ============================================================================
 *
 */
angular.module('OsgiMgmtApp', [])

	/** Injectable-constant with URL of the REST API */
	.constant('API_URL', 'http://localhost:8080/api/v1/')

	/** various util methods */
	.factory('osgiMgmtUtils', function() {
		return {
			/** Returns the CSS class for an OSGi Bundle's state */
			getBundleStateClass : function(candidate) {

				var labelClass = 'label-default';

				if (candidate) {

					if (typeof candidate !== 'string') {
						// Passed in object should have a property 'state' containing the (bundle) state as string
						candidate = candidate.state;
					}

					var candidateUp = candidate.toUpperCase();

					if (candidateUp === 'ACTIVE') {
						labelClass = 'label-success';
					} else if (candidateUp === 'RESOLVED') {
						labelClass = 'label-primary';
					} else if (candidateUp == 'INSTALLED') {
						labelClass = 'label-default';
					} else if (candidateUp == 'STARTING') {
						labelClass = 'label-info';
					}
				}
				return labelClass;
			},

			/** Returns the CSS class for an OSGi DS Component's state */
			getComponentStateClass : function(candidate) {

				var labelClass = 'label-default';

				if (candidate) {

					if (typeof candidate !== 'string') {
						// Passed in object should have a property 'state' containing the (bundle) state as string
						candidate = candidate.state;
					}

					var candidateUp = candidate.toUpperCase();

					if (candidateUp === 'ACTIVE') {
						labelClass = 'label-success';
					} else if (candidateUp === 'UNSATISFIED') {
						labelClass = 'label-danger';
					} else if (candidateUp == 'FACTORY') {
						labelClass = 'label-primary';
					} else if (candidateUp == 'REGISTERED') {
						labelClass = 'label-info';
					}
				}
				return labelClass;
			}

		}
	})


	/**
	 * Renders a Bootstrap label for an OSGi state
	 */
	.directive('osgiBundleStateLabel', function(osgiMgmtUtils) {
		return {
			restrict: 'A', // works as attribute
			template: '<span class="label {{labelClass}}">{{bundleState}}</span>',
			scope: {
				'state': '=?',
				'bundle': '=?'

			},
			replace: true,
			link: function (scope, elem, attrs) {

				var renderLabel = function() {
					var candidate = (scope.state ? scope.state : scope.bundle);

					scope.labelClass = osgiMgmtUtils.getBundleStateClass(candidate);
					scope.bundleState = (scope.state ? scope.state : scope.bundle.state);
				};

				scope.$watch('state', function(newVal, oldVal) {
					if(newVal) {
						renderLabel();
					}
				});

				scope.$watch('bundle', function(newVal, oldVal) {
					if(newVal) {
						renderLabel();
					}
				});
			}
		}
	})

	/**
	 * Renders a Bootstrap label for an DS Component state
	 */
	.directive('osgiDscomponentStateLabel', function(osgiMgmtUtils) {
		return {
			restrict: 'A', // works as attribute
			template: '<span class="label {{labelClass}}">{{componentState}}</span>',
			scope: {
				'state': '=?',
				'component': '=?'

			},
			replace: true,
			link: function (scope, elem, attrs) {

				var renderLabel = function() {
					var candidate = (scope.state ? scope.state : scope.component);

					scope.labelClass = osgiMgmtUtils.getComponentStateClass(candidate);
					scope.componentState = (scope.state ? scope.state : scope.component.state);
				};

				scope.$watch('state', function(newVal, oldVal) {
					if(newVal) {
						renderLabel();
					}
				});

				scope.$watch('component', function(newVal, oldVal) {
					if(newVal) {
						renderLabel();
					}
				});
			}
		}
	})

	/** Renders a 'badge' with the either the length of an array or the number of own properties on an object
 	*/
	.directive('osgiBadgeCounter', function() {
		return {
			restrict: 'A',
			replace: false,
			link: function(scope, element, attrs) {

				function getCount(o) {

					if (!o) {
						return 0;
					}

					if (angular.isArray(o)) {
						return o.length;
					}

					if (typeof (o) == 'object') {
						var pc = 0;

						for (var p in o) {
							if (o.hasOwnProperty(p)) {
								pc += 1;
							}
						}
						return pc;
					}

					// good luck
					return o;
				}

				scope.$watch(attrs.osgiBadgeCounter, function(value) {

					if (!element.hasClass('badge')) {
						element.addClass('badge');
					}

					var counter = getCount(value);

					element.text(counter);
				})
			}
		}
	})

	/** Renders a OSGi Bundle name including it's version and id */
	.directive('osgiBundleName', function() {
		return {
			restrict: 'A',
			replace: false,
			template: '{{symbolicName}}_{{version}} [{{id}}]',
			link: function(scope, element, attrs) {
				var bundle;

				scope.$watch(attrs.osgiBundleName, function(value) {
					scope.symbolicName = value.symbolicName;
					scope.version = value.version;
					scope.id = value.id;
				})
			}
		}
	})

;



