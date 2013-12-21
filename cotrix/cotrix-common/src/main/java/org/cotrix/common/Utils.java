package org.cotrix.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

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
	 * Throws an exception if a given object with a given name is null.
	 * @param name the name
	 * @param o the object
	 * @throws IllegalArgumentException if the object is null
	 */
	public static void notNulls(String name, Object[] o) throws IllegalArgumentException {
		notNull(name, o);
		for (Object ob : o)
			notNull("one of "+name, ob);
		
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
	 * Throws an exception if a given array with a given name is empty.
	 * @param name the name
	 * @param a the array
	 * @throws IllegalArgumentException if the array is null
	 */
	public static void notEmpty(String name, Object[] a) throws IllegalArgumentException {
		
		notNull(name,a);
		
		if (a.length==0)
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
	 * @throws IllegalArgumentException if the collection is null or empty
	 */
	public static void valid(String name, Collection<String> c) throws IllegalArgumentException {
		notNull(name, c);
		notEmpty(name,c);
		for (String e : c)
			valid(name+"'s element", e);
	}
	
	
	/**
	 * Throws an exception if a given array with a given name is null, empty, or contains null elements.
	 * @param name the name
	 * @param a the array
	 * @throws IllegalArgumentException if the array is null or empty
	 */
	public static void valid(String name, Object[] a) throws IllegalArgumentException {
		notNull(name,a);
		for (Object e : a)
			notNull(name+"'s element", e);
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
	
	/**
	 * Return a clone of a given name.
	 * @param name the name
	 * @return the clone
	 */
	public static QName copyName(QName name) {
		return new QName(name.getNamespaceURI(),name.getLocalPart());
	}
	
	
	/**
	 * Reveals a given runtime type of a given object.
	 * @param o the object
	 * @param type the type
	 * @return the object under the revealed type
	 */
	public static <T> T reveal(Object o, Class<T> type) {
		
		notNull("object",o);
		
		try {
			return type.cast(o);
		}
		catch(ClassCastException e) {
			throw new AssertionError("expected a "+type+ ", found instead a "+o.getClass());
		}
	}
	
	/**
	 * Reveals a given runtime type for all the objects in an {@link Iterable}.
	 * @param objects the iterable
	 * @param type the runtime type
	 * @return the objects under the revealed type
	 */
	public static <PUBLIC, PRIVATE extends PUBLIC > List<PRIVATE> revealAll(Iterable<? extends PUBLIC> objects, Class<PRIVATE> privateClass) {
		
		notNull("objects",objects);
		
		List<PRIVATE> privates = new ArrayList<PRIVATE>();
		
		for (PUBLIC publicObject : objects)
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

	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	
 	public static void validEmail(String email) {
		if (!pattern.matcher(email).matches())
			throw new IllegalArgumentException("invalid email address "+email);
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
	
	/**
	 * Gathers the elements of an {@link Iterable} into a collection.
	 * @param it the iterable
	 * @return the collection
	 */
	public static <T>  Collection<T> collect(Iterable<T> it) {
		
		Collection<T> c = new ArrayList<T>();
		for (T t : it)
			c.add(t);
		return c;
			
	}
	
	/**
	 * Asserts that a collection has a given distinct elements
	 * @param c1 the collection
	 * @param ts the elements
	 * 
	 * @throws AssertionError if the distinct elements of the collection differ from the given ones.
	 */
	public static void assertEqualSets(Collection<?> c1, Object ... ts) {
		
		HashSet<?> s1 = new HashSet<Object>(c1);
		HashSet<?> s2 = new HashSet<Object>(Arrays.asList(ts));
		if (!s1.equals(s2))
				throw new AssertionError("expected "+s2+" but was "+s1);
		
	}
	
	
	
}
