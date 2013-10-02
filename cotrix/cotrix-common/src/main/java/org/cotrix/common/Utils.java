package org.cotrix.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * App-wide utilities.
 * 
 * @author Fabio Simeoni
 *
 */
public class Utils {

	/**
	 * Throws an exception if a given object with a given name is null.
	 * @param name the name
	 * @param o the object
	 * @throws IllegalArgumentException if the object is null
	 */
	public static void notNull(String name, Object o) throws IllegalArgumentException {
		if (o==null)
			throw new IllegalArgumentException(name+" is null");
	}
	
	/**
	 * Throws an exception if a given string with a given name is empty.
	 * @param name the name
	 * @param s the string
	 * @throws IllegalArgumentException if the string is null
	 */
	public static void notEmpty(String name, String s) throws IllegalArgumentException {
		if (s.isEmpty())
			throw new IllegalArgumentException(name+" is empty");
	}
	
	/**
	 * Throws an exception if a given string with a given name is null or empty.
	 * @param name the name
	 * @param s the string
	 * @throws IllegalArgumentException if the string is null or empty
	 */
	public static void valid(String name, String s) throws IllegalArgumentException {
		notNull(name, s);
		notEmpty(name,s);
	}
	
	/**
	 * Throws an exception if a given collection with a given name is empty.
	 * @param name the name
	 * @param c the collection
	 * @throws IllegalArgumentException if the collection is null
	 */
	public static void notEmpty(String name, Collection<? extends Object> c) throws IllegalArgumentException {
		if (c.isEmpty())
			throw new IllegalArgumentException(name+" is empty");
	}

	/**
	 * Throws an exception if a given collection of a strings with a given name is null, empty, or contains null or empty elements.
	 * @param name the name
	 * @param c the collection
	 * @throws IllegalArgumentException if the collection is null or emopt
	 */
	public static void valid(String name, Collection<String> c) throws IllegalArgumentException {
		notNull(name, c);
		notEmpty(name,c);
		for (String e : c)
			valid(name+"'s element", e);
	}
	
	
	/**
	 * Throws an exception if a given qualified with a given name is null or empty.
	 * @param text the name
	 * @param name the qualified name
	 * @throws IllegalArgumentException if the qualified name is null or empty
	 */
	public static void valid(String text,QName name) throws IllegalArgumentException {
		notNull(text, name);
		valid(text,name.getLocalPart());
	}
	
	public static QName copyName(QName name) {
		return new QName(name.getNamespaceURI(),name.getLocalPart());
	}
	
	
	public static <T> T reveal(Object publicObject, Class<T> privateClass) {
		
		notNull("object",publicObject);
		
		try {
			return privateClass.cast(publicObject);
		}
		catch(ClassCastException e) {
			throw new IllegalArgumentException("expected a "+privateClass+ "found instead a "+publicObject.getClass());
		}
	}
	
	public static <PUBLIC, PRIVATE extends PUBLIC > List<PRIVATE> reveal(Iterable<? extends PUBLIC> publicObjects, Class<PRIVATE> privateClass) {
		
		notNull("objects",publicObjects);
		
		List<PRIVATE> privates = new ArrayList<PRIVATE>();
		
		for (PUBLIC publicObject : publicObjects)
			privates.add(reveal(publicObject,privateClass));
		
		return privates;
	}
	
	/**
	 * Throws an exception if a given file is null or does not exist or is a directory or cannot be read.
	 * @param file the file
	 * @throws IllegalArgumentException if th file is null or does not exist or is a directory or cannot be read
	 */
	public static void valid(File file) throws IllegalArgumentException {
		
		notNull("file", file);
		
		if (!file.exists() || file.isDirectory() || !file.canRead())
			throw new IllegalArgumentException(file+" does not exist, is a directory, or cannot be read");
	}
	
	
	/**
	 * Returns a a {@link RuntimeException} that wraps a given fault.
	 * @param t the fault
	 * @return the exception
	 */
	public static RuntimeException unchecked(Throwable t) {

		return unchecked(t.getMessage()+"( unchecked wrapper )",t);

	}

	/**
	 * Returns a {@link RuntimeException} that wraps a given fault with a given message.
	 * @param msg the message
	 * @param t the fault
	 * @return the exception
	 */
	public static RuntimeException unchecked(String msg, Throwable t) {

		return (t instanceof RuntimeException) ? RuntimeException.class.cast(t) : new RuntimeException(msg,
				t);

	}

	/**
	 * Re-throws a given fault wrapped in a {@link RuntimeException}.
	 * @param t the fault
	 * @throws RuntimeException the exception
	 */
	
	public static void rethrowUnchecked(Throwable t) throws RuntimeException {

		throw unchecked(t);

	}
	
	/**
	 * Re-throws a given fault wrapped in a {@link RuntimeException}, with a given message.
	 * @param msg the message
	 * @param t the fault
	 * @throws RuntimeException the exception
	 */
	public static void rethrow(String msg,Throwable t) throws RuntimeException {
		
		throw unchecked(msg,t);

	}
	
//	public static boolean isActive(Instance<?> objects, String profile) {
//		
//		notNull("input", objects);
//		
//		Iterator<?> it = objects.iterator();
//		
//		while (it.hasNext()) {
//		
//			Class<?> type = it.next().getClass();
//			if (type.isAnnotationPresent(Active.class)) {
//				if (!type.getAnnotation(Active.class).value().equals(profile))
//				 return false;
//			}
//		}
//		return true;
//	}

	
}
