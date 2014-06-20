package org.cotrix.domain.dsl.grammar;

import java.util.Collection;
import java.util.List;

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

		/**
		 * Adds one or more attributes to the object.
		 * 
		 * @param attributes the attributes
		 * @return the next clause in the sentence
		 */
		C attributes(Attribute... attributes);
		
		
		/**
		 * Adds one or more attributes to the object.
		 * 
		 * @param attributes the attributes
		 * @return the next clause in the sentence
		 */
		C attributes(Collection<Attribute> attributes);
	}
	
	public static interface LinksClause<T,C> {

		/**
		 * Adds one or more links to the object.
		 * 
		 * @param links the links
		 * @return the next clause in the sentence
		 */
		@SuppressWarnings("unchecked")
		C links(T ... links);
		
		/**
		 * Adds one or more links to the object.
		 * 
		 * @param links the links
		 * @return the next clause in the sentence
		 */
		C links(Collection<T> links);
	}
	
	public static interface LinkTargetClause<T,C> {

		/**
		 * Adds a link target to the object.
		 * 
		 * @param target the target
		 * @return the next clause in the sentence
		 */
		C target(T target);
	}

	
	public static interface WithManyClause<T,C> {

		/**
		 * Adds one or more parameters to the object.
		 * 
		 * @param parameters the parameters
		 * @return the next clause in the sentence
		 */
		@SuppressWarnings("unchecked")
		C with(T ... parameters);
		
		
		/**
		 * Adds one or more parameters to the object.
		 * 
		 * @param parameters the parameters
		 * @return the next clause in the sentence
		 */
		C with(List<T> parameters);
	}
	
	public static interface NameClause<C> {
		
		/**
		 * Sets the name of the object.
		 * 
		 * @param name the name
		 * @return the next clause in the sentence
		 */
		C name(QName name);
		
		/**
		 * Sets the name of the object.
		 * 
		 * @param name the name
		 * @return the next clause in the sentence
		 */
		C name(String name);
	}
	
	
	public static interface DeleteClause<DELETE> {
		
		/**
		 * Marks this object for deletion.
		 * @return the next clause in the sentence
		 */
        DELETE delete();
	}
	
	public static interface VersionClause<T> {

		/**
		 * Sets the version of the object.
		 * 
		 * @param version the version
		 * @return the next clause in the sentence
		 */
		BuildClause<T> version(String version);
	}
	
	public static interface BuildClause<T> {

		/**
		 * Closes the sentence and returns the object.
		 * 
		 * @return the object
		 */
		T build();
	}
}
