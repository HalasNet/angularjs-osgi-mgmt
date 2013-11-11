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

package nh.angularjsosgi.http;

import org.springframework.util.StringUtils;

/**
 * @author Nils Hartmann <nils@nilshartmann.net>
 * 
 */
public class Utils {
	public static String combine(String pattern1, String pattern2) {
		if (!StringUtils.hasText(pattern1) && !StringUtils.hasText(pattern2)) {
			return "";
		} else if (!StringUtils.hasText(pattern1)) {
			return pattern2;
		} else if (!StringUtils.hasText(pattern2)) {
			return pattern1;
		}

		if (pattern1.endsWith("/") && pattern2.startsWith("/")) {
			if (pattern1.length() == 1) {
				return pattern2;
			}

			return pattern1.substring(1) + pattern2;
		}

		if (pattern1.endsWith("/") || pattern2.startsWith("/")) {
			return pattern1 + pattern2;
		}

		return pattern1 + "/" + pattern2;
	}

	public static String normalize(String str) {
		if (str == null) {
			return null;
		}

		StringBuilder result = new StringBuilder();

		boolean delim = false;

		for (int i = 0; i < str.length(); i++) {
			char cc = str.charAt(i);

			if (cc == '/') {
				if (delim) {
					continue;
				}

				delim = true;
			} else {
				delim = false;
			}

			result.append(cc);
		}

		return result.toString();
	}

}
