module.exports = function(config){
	config.set({
		basePath : '../',

		files : [
			'app/libs/angularjs_1.2.0-rc.3/angular.js',
			'app/libs/*/*.js',
			'test/lib/angular/angular-mocks.js',
			'app/**/*.js',
			'test/unit/**/*.js'
		],

		exclude : [
			'app/lib/angular/angular-loader.js',
			'app/lib/angular/*.min.js'
		],

		autoWatch : true,

		frameworks: ['jasmine'],

		browsers : ['Chrome'],

		plugins : [
			'karma-junit-reporter',
			'karma-chrome-launcher',
			'karma-firefox-launcher',
			'karma-jasmine'
		],

		junitReporter : {
			outputFile: 'test_out/unit.xml',
			suite: 'unit'
		}

	})}