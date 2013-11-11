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
package nh.angularjsosgi.util.json;

import java.io.InputStream;
import java.io.Reader;

/**
 * Defines a Service that is able to convert objects to JSON
 * 
 * Instances should be registered at the service runtime to make them available
 * for consumers
 * 
 * @author nils
 * 
 */
public interface JsonService {

	/**
	 * Converts the specified object ot json and writes the JSON-ified object to
	 * the given Appendable
	 * 
	 * @param appendable
	 * @param object
	 */
	void toJson(Appendable appendable, Object object);

	/**
	 * @param reader
	 * @param type
	 * @return
	 */
	<T> T fromJson(Reader reader, Class<T> type);

	/**
	 * @param is
	 * @param type
	 * @return
	 */
	<T> T fromJson(InputStream is, Class<T> type);

}
