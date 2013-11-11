/** from: http://www.mobtowers.com/load-cross-domain-javascript-synchronously-using-jquery/ */
function loadAndExecuteScripts(aryScriptUrls, index, callback) {
	$.getScript(aryScriptUrls[index],function(){
		if(index + 1 <= aryScriptUrls.length - 1){
			loadAndExecuteScripts(aryScriptUrls, index + 1, callback);
		} else {
			if(callback)
				callback();
		}
	});
}

console.log("LOADING CLIENT CONFIGURATION");
$.getJSON( "client-config.json", function( data ) {

	// Required Platform/Framework modules
	var PLATFORM_MODULES = [
		'ngRoute',
		'ngAnimate',
		'Platform.Ui.Client',
		'Platform.Ui.Components',
		'Platform.PerspectiveManager'
	];

	// All modules loaded from client configuration
	var MODULES = [];

	var SCRIPTS = [];

	// Load all modules defined in client configuration

	console.log("MODULES LENGTH: " + data.modules.length);
	// Load all Scripts sequential (to make sure that a module's definition
	// is not loaded before a script that actually uses the module)

	for (var m = 0; m<data.modules.length;m++) {
		console.log("LOAD MODULE: " + data.modules[m]);
		var module = data.modules[m];

		MODULES.push({'id':module.name,'basedir': module.basedir});

		var basedir = module.basedir;

		var scripts = module.scripts.split(",");

		for (var i=0;i<scripts.length;i++) {
			var script = basedir + '/' + scripts[i].trim();
			console.log('Add Script: ' + script);
			SCRIPTS.push(script);
		}
	}


	loadAndExecuteScripts(SCRIPTS, 0, function() {
		// Bootstrap Angular...
		console.log("CLIENT CONFIG LOADED. BOOTSTRAPPING ANGULAR");

		angular.element(document).ready(function() {
			console.log("Bootstrap Platform.MainApp")
			var allModules = PLATFORM_MODULES;

			// Prepare Modules
			$.each(MODULES, function(id, module) {
				// add module to list of all modules
				allModules.push(module.id);

				// create a injectable constant with the configured basedir
				var constantName = module.id.replace(/\./g,'_').toUpperCase()+"_BASEDIR";

				angular.module(module.id).constant(constantName, module.basedir);
			});

			// Create 'overall application'
			angular.module('Platform.MainApp', allModules);

			// Bootstrap AngularJs
			angular.bootstrap(document, ['Platform.MainApp']);
		})
	});

});

