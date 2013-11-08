package org.example;

import javax.servlet.http.HttpServlet;

import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;

import nh.angularjsosgi.controller.Controller;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Component(immediate=true)
public class ExampleComponent {
	
	private UnavailableService unavailableService;
	
	Gson gson = null;

	// TODO: class provided by template

	@Activate
	public void sayHello() {
		
		System.out.println("HAllo?");
		
		HttpServlet httpServlet = null;
		
	}
	
	HttpServlet servlet;
	
	
	class MyController extends Controller {
		
	}
	
	@Reference
	public void setUnavailableService(UnavailableService unavailableService) {
		this.unavailableService = unavailableService;
	}
	
	@Reference(optional = true)
	public void setOptionalService(UnavailableService2 unavailableService2) {
		
	}
	
	@Reference(multiple=true,optional=false)
	public void setHttpService(HttpService httpService) {
		System.out.println("BIND HTTP SERVICE: " + httpService);
		// noop
	}
	
	@Reference(target="(myProp=eins)",optional = true)
	public void setLogService(LogService logService) {
		
	}
	
	
	
	
}