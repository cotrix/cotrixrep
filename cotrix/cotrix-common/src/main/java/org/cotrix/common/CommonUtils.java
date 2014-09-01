package org.cotrix.common;

import static java.text.DateFormat.*;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;


public class CommonUtils {

	//helpers

	private final static XStream stream = new XStream();
	
	private final static Gson jsonStream = new Gson();

	
	public static String time() {
		return getDateTimeInstance().format(Calendar.getInstance().getTime());
	}
	
	public static Date time(String time) {
		try {
			return getDateTimeInstance().parse(time);
		}
		catch(ParseException e) {
			throw unchecked("invalid date format",e);
		}
	}
	public static XStream binder() {

		return stream; 
	
	}
	
	public static Gson jsonBinder() {

		return jsonStream; 
	
	}
	

	
	//parameter validation
	
	public static void verify(String msg, boolean expression) {
	
		if (!expression)
			throw new IllegalArgumentException(msg);
	}
	
	
	public static void notNull(String name, Object o) throws IllegalArgumentException {
		
		verify(name+" is null",o!=null);
	
	}
	
	//strings 
	
	public static void notEmpty(String name, String s) throws IllegalArgumentException {

		notNull(name, s);
		verify(name+" is empty", !s.isEmpty());
	
	}
	
	
	public static void valid(String name, String s) throws IllegalArgumentException {

		notNull(name, s);
		notEmpty(name,s);
	
	}
	
	//numbers
	
	public static void positive(String name, int val) throws IllegalArgumentException {
	
		verify(name+" is not positive",val>0);
	
	}
	
	
	//collections
	
	public static void notNulls(String name, Object[] o) throws IllegalArgumentException {
		
		notNull(name, o);
		
		for (Object ob : o)
			notNull("one of "+name, ob);
		
	}
	
	public static void notEmpty(String name, Collection<? extends Object> c) throws IllegalArgumentException {

		notNull(name, c);
		verify(name+" is empty", !c.isEmpty());
	}
	
	public static void notEmpty(String name, Object[] a) throws IllegalArgumentException {
		
		notNull(name,a);
		verify(name+" is empty",a.length>0);
	}

	public static void valid(String name, Object[] a) throws IllegalArgumentException {
		
		notNull(name,a);
		
		for (Object e : a)
			notNull(name+"'s element", e);
	}
	
	public static void valid(String name, Collection<String> c) throws IllegalArgumentException {

		notNull(name, c);
		notEmpty(name,c);
		
		for (String e : c)
			valid(name+"'s element", e);
	}
	
	
	//files
	public static void valid(File file) throws IllegalArgumentException {
	
		notNull("file", file);
		verify(file+" does not exist, is a directory, or cannot be read",file.exists() && file.isDirectory() && file.canRead());
	}
	
	public static void validDirectory(File dir) throws IllegalArgumentException {
		
		notNull("directory", dir);
		verify(dir+" does not exist or cannot be read",dir.exists() && dir.canRead());
	}
	
	public static boolean isValid(File file) {
		
		notNull("file", file);
		return file.exists() && file.isDirectory() && file.canRead();
	}
	
	public static boolean isValidDirectory(File dir) {
		
		notNull("directory", dir);
		return dir.exists() && dir.canRead();
	}
	
	//emails
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	
 	public static void validEmail(String email) {
 		
 		verify("invalid email address "+email,pattern.matcher(email).matches());
	
 	}
	
	
	// other
	
	public static void valid(String text,QName name) throws IllegalArgumentException {
		notNull(text, name);
		valid(text,name.getLocalPart());
	}
	
	
	
	//casts
	
	public static <T> T reveal(Object o, Class<T> type) {
		
		notNull("object", o);
		
		try {
			return type.cast(o);
		}
		catch(ClassCastException e) {
			throw new AssertionError("expected a "+type+ ", found instead a "+o.getClass());
		}
	}
	
	public static <PUBLIC, PRIVATE extends PUBLIC > List<PRIVATE> revealAll(Iterable<? extends PUBLIC> objects, Class<PRIVATE> privateClass) {
		
		notNull("objects",objects);
		
		List<PRIVATE> privates = new ArrayList<PRIVATE>();
		
		for (PUBLIC publicObject : objects)
			privates.add(reveal(publicObject,privateClass));
		
		return privates;
	}
	

	
	//faults
	
	public static RuntimeException unchecked(Throwable t) {

		return unchecked(t.getMessage()+"( unchecked wrapper )",t);

	}

	public static RuntimeException unchecked(String msg, Throwable t) {

		return (t instanceof RuntimeException) ? 
				RuntimeException.class.cast(t) : 
				new RuntimeException(msg,t);

	}

	public static void rethrowUnchecked(Throwable t) throws RuntimeException {

		throw unchecked(t);

	}
	
	public static void rethrow(String msg,Throwable t) throws RuntimeException {
		
		throw unchecked(msg,t);

	}
	
	public static <T>  Collection<T> collect(Iterator<T> it) {
		
		Collection<T> c = new ArrayList<T>();
		
		while (it.hasNext()) 
			c.add(it.next());
		
		return c;
			
	}

	public static <T>  Collection<T> collect(Iterable<T> it) {
		
		Collection<T> c = new ArrayList<T>();
		
		for (T t : it)
			c.add(t);
		return c;
			
	}
	
	public static <T> Set<T> collectUnordered(Iterable<T> it) {
		
		Set<T> c = new HashSet<T>();
	
		for (T t : it)
			c.add(t);
		return c;
			
	}
	
	public static <T> int count(Iterable<T> elements) {
		return count(elements.iterator());
	}
	
	public static <T> int count(Iterator<T> elements) {
		int count = 0;
		while(elements.hasNext()) {
			elements.next();
			count++;
		}
		return count;
	}
	
	public static void assertEqualUnordered(Collection<?> c1, Object ... ts) {
		
		HashSet<?> s1 = new HashSet<Object>(c1);
		HashSet<?> s2 = new HashSet<Object>(Arrays.asList(ts));
		if (!s2.equals(s1))
				throw new AssertionError("expected "+s2+" but was "+s1);
		
	}
	
	public static void assertEqualOrdered(Collection<?> c1, Object ... ts) {
		
		Collection<?> s1 = new ArrayList<Object>(c1);
		Collection<?> s2 = Arrays.asList(ts);
		if (!s2.equals(s1))
				throw new AssertionError("expected "+s2+" but was "+s1);
		
	}
	
	
	
	
	
}
