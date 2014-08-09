package org.cotrix.domain.common;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Named;

/**
 * SPI for collections of named beans.
 * 
 * @author Fabio Simeoni
 */
public interface BeanContainer<B extends Named.Bean> extends Iterable<B> {
		
	
	int size();
	
	
	void add(B element);
	
	void remove(String id);
	
	
	//id-based access
	
	boolean contains(String id);
	
	B lookup(String id); //may be null
	
	Collection<B> get(Collection<String> ids); 
	
	
	//name-based access
	
	boolean contains(QName name);
	
	B getFirst(QName name); //null or first
	
	Collection<B> get(QName name);  
	
	
}
