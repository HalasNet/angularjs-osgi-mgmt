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
angular.module('Platform.Ui.Components',[])
	.directive('collapsePanel', function() {

		return {
			restrict: 'E', // works as attribute
			templateUrl: '/platform/ui/templates/collapse-panel.html',
			transclude: true,
			replace: true,
			scope: true,
			compile: function(elem, attrs, transcludeFn) {
				return function (scope, template, attrs) {

					function replaceHeaderAndBodyTemplate(template, clone) {
						// Fill template content
						var headerTemplate = template.find('div.panel-heading');
						var bodyTemplate = template.find('div.panel-body');

						var headerElem = clone.filter('collapse-panel-header');
						var bodyElem = clone.filter('collapse-panel-body');

						// Add *content* of HEADER element in document to template
						// (that is exclude/skip the surrounding collapse-panel-header element)
						angular.forEach(headerElem, function(value) {
							headerTemplate.append(value);
						});

						// Add *content* of BODY element in document to template
						// (that is exclude/skip the surrounding collapse-panel-body element)
						angular.forEach(bodyElem, function(value) {
							bodyTemplate.append(value);
						});
					}

					function updateToggleState(toggleModel, expand) {

						if (expand) {
							toggleModel.expanded = true;
							toggleModel.toggleIcon = 'glyphicon-chevron-up'
							toggleModel.state = 'in';
						} else {
							toggleModel.expanded = false;
							toggleModel.toggleIcon = 'glyphicon-chevron-down'
							toggleModel.state = '';
						}


						return toggleModel;
					};



					transcludeFn(scope, function(clone) {

						replaceHeaderAndBodyTemplate(template, clone);

						// create initial model...
						scope.toggleModel = {};

						/*
						if (attrs.id) {
							console.log("SET ID: " + attrs.id);
							scope.toggleModel.id = 'panel' + attrs.id;
						}
						*/

						// watch for changes of expanded attribute
						scope.$watch(attrs.expanded, function(expandedAttr) {
							var newState;
							if (typeof expandedAttr === 'boolean') {
								newState = expandedAttr;
							} else {
								if (!expandedAttr) {
									// collapse by default
									newState = false;
								} else {
									newState = (expandedAttr === 'true');
								}
							}
							updateToggleState(scope.toggleModel, newState);
						});

						// register toggleCollapse method to programtically change collapse state
						scope.toggleCollapse = function() {
							var toggleModel = scope.toggleModel;
							var newState = !toggleModel.expanded;
							updateToggleState(toggleModel, newState);
						};
					});
				}
			}
		}
	})

	.directive('tableSortable', function() {
		return {
			restrict: 'C',
			scope: true,
			link : function(scope, element, attrs) {

				/** All Headers */
				var ths = [];

				/** Current orderKey */
				scope.orderKey = '';

				function setOrderColumn(th) {

					var key = th.attr('order-key');

					// STEP 1: UPDATE MODEL
					if (scope.orderKey === key) {
						// toggle currently active ordering
						scope.orderKey = '-'+key;
					} else {
						scope.orderKey = key;
					}

					// STEP 2: UPDATE UI
					angular.forEach(ths, function(value) {

						// remove up/down icons
						value.span.removeClass('glyphicon-arrow-up');
						value.span.removeClass('glyphicon-arrow-down');

						// add icon on current order column
						if (value.th === th) {
							if (scope.orderKey.charAt(0)=='-') {
								value.span.addClass('glyphicon-arrow-up');
							} else {
								value.span.addClass('glyphicon-arrow-down');
							}
						}
					});
				}

				// Initialize Header Elements
				angular.forEach(element.find('th'), function(value) {
					var th = $(value);

					if (angular.isDefined(th.attr("order-key"))) {
						// span for order icon
						var span = $("<span class='order-indicator glyphicon pull-right'></span>");
						th.append(span);
						ths.push({'th':th, 'span': span});

						// add listener
						th.on('click', function(event) {
							scope.$apply(function() {
								setOrderColumn(th)
							})
						});

						// set default order column if defined
						if (angular.isDefined(th.attr('default-order'))) {
							setOrderColumn(th);
						}
					}
				});
			}

		}
	})

	.directive('tooltip', function () {
			return {
			restrict: 'A',

			link : function(scope, element, attrs) {
				if (angular.isDefined(attrs.tooltip) && attrs.tooltip!='') {
					$(element).tooltip({
						animation: true,
						title: attrs.tooltip
					});
				}
			}
		}
	})


	.directive('scrollTo', function ($location, $anchorScroll) {
		return function(scope, element, attrs) {
			element.bind('click', function(event) {
				var items = attrs.scrollTo.split(":");

				var targetId = items[0];
				var offset = 0;
				if (items.length!=1) {
					// get element we should take the offset from
					var offsetElement = $(items[1]);

					offset = offsetElement.position().top;
				}

				var top = $(targetId).offset().top - offset;
				$('html,body').animate({scrollTop: top}, 500);

			})
		}
	})

;