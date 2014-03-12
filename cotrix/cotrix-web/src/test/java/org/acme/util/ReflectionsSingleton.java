/**
 * 
 */
package org.acme.util;

import org.reflections.Reflections;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ReflectionsSingleton {
	
	private static Reflections reflections;
	
	public static Reflections getReflections() {
		if (reflections == null) reflections = new Reflections("org.cotrix.web");
		return reflections;
	}

}
