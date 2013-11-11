'use strict';

/* jasmine specs for controllers go here */

describe('OSGiMgmtApp', function(){

	beforeEach(function() {
		module('OsgiMgmtApp');
	});


	it('should have an API_URL constant', inject(function(API_URL) {
		expect(API_URL).not.toBe(null);
		expect(API_URL).toBe('http://localhost:8080/api/v1/');
	}));

	describe('osgiMgmtUtils', function() {

		it('should return \'label-default\' for string INSTALLED', inject(function(osgiMgmtUtils) {
			expect(osgiMgmtUtils.getBundleStateClass('INSTALLED')).toBe('label-default');
		}));

		it('should return \'label-default\' for an INSTALLED bundle', inject(function(osgiMgmtUtils) {
			expect(osgiMgmtUtils.getBundleStateClass({'state': 'INSTALLED'})).toBe('label-default');
		}));

		it('should render a default string for a bundle', inject(function(osgiMgmtUtils, $compile, $rootScope) {
			var scope = $rootScope.$new();
			scope.bundle = {
				'symbolicName':'org.hello.world',
				'version': '1.0.0',
				'id': '666'
			};
			var element = ($compile('<span osgi-bundle-name="bundle"></span>')(scope));
			scope.$digest();

			expect(element[0].innerHTML).toBe('org.hello.world_1.0.0 [666]');
		}));
	})

});