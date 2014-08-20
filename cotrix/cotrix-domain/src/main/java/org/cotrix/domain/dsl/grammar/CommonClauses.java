package org.cotrix.domain.dsl.grammar;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;


public class CommonClauses {

	
	public static interface AttributeClause<T,C> {

		C attributes(Attribute... attributes);
		
		C attributes(Collection<Attribute> attributes);
		
		C attributes(AttributeGrammar.FourthClause ... attributes);
	}

	
	public static interface NameClause<C> {
		
		C name(QName name);
		
		C name(String name);
	}
	
	
	public static interface DeleteClause<DELETE> {
		
		DELETE delete();
	}
	
	public static interface BuildClause<T> {

		T build();
	}
}
