package nh.angularjsosgi.rest.osgi.equinox;

import org.eclipse.osgi.service.resolver.VersionConstraint;

import nh.angularjsosgi.rest.osgi.model.AbstractRestObject;

public class UnresolvedConstraint extends AbstractRestObject {

	private String type;
	private String name;
	
	 
	 
	public static UnresolvedConstraint fromVersionConstraint(
			VersionConstraint versionConstraint) {

		UnresolvedConstraint c = new UnresolvedConstraint();
		c.type = versionConstraint.getRequirement().getNamespace();
		c.name = versionConstraint.getName();
		
		return c;
	}
	
	

}
