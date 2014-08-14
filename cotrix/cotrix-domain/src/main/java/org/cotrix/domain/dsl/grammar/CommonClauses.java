package org.cotrix.domain.dsl.grammar;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;

/**
 * Sentence clauses shared across grammars.
 * 
 * @author Fabio Simeoni
 *
 */
public class CommonClauses {

	
	public static interface AttributeClause<T,C> extends BuildClause<T> {

		C attributes(Attribute... attributes);
		
		C attributes(Collection<Attribute> attributes);
		
		C attributes(AttributeGrammar.OptionalClause ... attributes);
	}
	
	public static interface LinksClause<T,C> {

		@SuppressWarnings("unchecked")
		C links(T ... links);
		
		C links(Collection<T> links);
	}
	
	public static interface LinkTargetClause<T,C> {

		C target(T target);
	}

	
	public static interface NameClause<C> {
		
		C name(QName name);
		
		C name(String name);
	}
	
	
	public static interface DeleteClause<DELETE> {
		
		DELETE delete();
	}
	
	public static interface VersionClause<T> {

		BuildClause<T> version(String version);
	}
	
	public static interface BuildClause<T> {

		T build();
	}
}
