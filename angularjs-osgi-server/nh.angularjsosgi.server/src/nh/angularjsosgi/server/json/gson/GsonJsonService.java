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
package nh.angularjsosgi.server.json.gson;

import java.lang.reflect.Field;

import nh.angularjsosgi.server.json.JsonService;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A Gson-based implementation of a JsonService
 * 
 * @author nils
 * 
 */
@Component
public class GsonJsonService implements JsonService {

	private Gson _gsonParser;

	@Override
	public void toJson(Appendable appendable, Object object) {

		if (object != null) {
			_gsonParser.toJson(object, appendable);
		}
	}

	@Activate
	public void createParser() {
		GsonBuilder builder = new GsonBuilder() //
				.setFieldNamingStrategy(new UnderscoreFieldNamingStrategy()) //
				.setPrettyPrinting() //
		;

		_gsonParser = builder.create();
	}

	/**
	 * Removes leading underscores from field names
	 * 
	 */
	class UnderscoreFieldNamingStrategy implements FieldNamingStrategy {
		@Override
		public String translateName(Field field) {
			if (field.getName().startsWith("_")) {
				return field.getName().substring(1);
			}
			return field.getName();
		}
	}

}
