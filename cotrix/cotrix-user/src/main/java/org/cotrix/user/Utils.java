package org.cotrix.user;

import java.io.File;
import java.util.Collection;

/**
 * Module-wide utilities
 * @author Fabio Simeoni
 *
 */
public class Utils {
	
	public static void notNull(Object o) throws IllegalArgumentException {
		notNull("argument",o);
	}
	
	public static void notNull(String name, Object o) throws IllegalArgumentException {
		if (o==null)
			throw new IllegalArgumentException(name+" is null");
	}
	
	public static void notEmpty(String name, String o) throws IllegalArgumentException {
		if (o.isEmpty())
			throw new IllegalArgumentException(name+" is empty");
	}
	
	public static void notEmpty(String name, Collection<? extends Object> o) throws IllegalArgumentException {
		if (o.isEmpty())
			throw new IllegalArgumentException(name+" is empty");
	}
	
	public static void valid(String name, String o) throws IllegalArgumentException {
		notNull(name, o);
		notEmpty(name,o);
	}
	
	public static void valid(String name, Collection<String> o) throws IllegalArgumentException {
		notNull(name, o);
		notEmpty(name,o);
		for (String e : o)
			valid(name+"'s element", e);
	}
	
	public static void valid(File file) throws IllegalArgumentException {
		
		notNull("file", file);
		
		if (!file.exists() || file.isDirectory() || !file.canRead())
			throw new IllegalArgumentException(file+" does not exist, is a directory, or cannot be read");
	}
	

}
