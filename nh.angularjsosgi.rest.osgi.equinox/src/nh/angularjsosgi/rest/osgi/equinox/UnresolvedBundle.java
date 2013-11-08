package nh.angularjsosgi.rest.osgi.equinox;

import java.util.LinkedList;
import java.util.List;

import nh.angularjsosgi.rest.osgi.model.InstalledBundleId;

public class UnresolvedBundle {
	

	private InstalledBundleId bundle;
	private List<UnresolvedConstraint> unresolvedConstraints;
	
	public UnresolvedBundle(InstalledBundleId bundle) {
		this.bundle = bundle;
		this.unresolvedConstraints = new LinkedList<UnresolvedConstraint>();
	}
	
	void addUnresolvedConstraint(UnresolvedConstraint c) {
		unresolvedConstraints.add(c);
	}

}
