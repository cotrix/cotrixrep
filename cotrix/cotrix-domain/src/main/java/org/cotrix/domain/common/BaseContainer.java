package org.cotrix.domain.common;

import java.util.Collection;

import javax.xml.namespace.QName;

//common to entity and bean containers
public interface BaseContainer<T> extends Iterable<T> {
	
	int size();
	
	
	//id-based access
	
	boolean contains(String id);
	
	T lookup(String id); //may be null
	
	
	//name-based access
	
	boolean contains(QName name);
	
	T getFirst(QName name); //null or first
	
	Collection<T> get(QName name);  
	
	
}
