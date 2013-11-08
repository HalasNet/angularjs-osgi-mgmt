package org.example;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class OtherUsingComponent {

	@Reference
	public void setOtherComponent(OtherComponent component) {
		
	}
	
	
}
