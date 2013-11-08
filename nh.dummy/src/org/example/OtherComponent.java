package org.example;

import org.osgi.service.http.HttpService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

@Component(provide = OtherComponent.class,enabled=false)
public class OtherComponent {
	
	@Activate
	public void activate() {
		System.out.println(" ACTIVATED !!!!");
	}
	
	@Deactivate
	public void deactivate() {
		System.out.println("  DEACTIVATED");
	}
	
	@Reference
	public void setHttpService(HttpService httpService) {
		
	}

}
