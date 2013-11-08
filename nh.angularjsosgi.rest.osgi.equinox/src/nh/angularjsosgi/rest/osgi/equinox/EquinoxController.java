package nh.angularjsosgi.rest.osgi.equinox;

import java.util.Hashtable;
import java.util.Map;

import nh.angularjsosgi.controller.Controller;
import nh.angularjsosgi.controller.RequestMapping;
import nh.angularjsosgi.controller.Response;
import nh.angularjsosgi.rest.osgi.controller.AbstractOsgiController;
import nh.angularjsosgi.rest.osgi.model.InstalledBundleId;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.PlatformAdmin;
import org.eclipse.osgi.service.resolver.VersionConstraint;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(provide = Controller.class)
public class EquinoxController extends AbstractOsgiController {
	
	private BundleContext bundleContext;
	private PlatformAdmin platformAdmin;
	
	@RequestMapping("GET /root-resolve-errors")
	public void getRootResolveErrors(Response response) throws Exception {
		
		
		
		BundleDescription[] bundles = platformAdmin.getState().getBundles();
		
		VersionConstraint[] unsatisfiedLeaves = platformAdmin.getStateHelper().getUnsatisfiedLeaves(bundles);
		
		Map<Long, UnresolvedBundle> unresolvedBundles = new Hashtable<Long, UnresolvedBundle>();
		
		for (VersionConstraint versionConstraint : unsatisfiedLeaves) {
			BundleDescription bundleDescription = versionConstraint.getBundle();;
			Bundle declaringBundle = bundleContext.getBundle(bundleDescription.getBundleId());
			
			UnresolvedBundle unresolvedBundle = unresolvedBundles.get(declaringBundle.getBundleId());
			if (unresolvedBundle == null) {
				unresolvedBundle = new UnresolvedBundle(InstalledBundleId.fromBundle(declaringBundle));
			}
			
			UnresolvedConstraint c = UnresolvedConstraint.fromVersionConstraint(versionConstraint);
			unresolvedBundle.addUnresolvedConstraint(c);
		}
		
		
		response.json(unresolvedBundles.values());
	
	}
	
	
	
	@Activate
	public void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
	}
	
	@Reference
	public void setPlatformAdmin(PlatformAdmin platformAdmin) {
		this.platformAdmin = platformAdmin;
	}

}
