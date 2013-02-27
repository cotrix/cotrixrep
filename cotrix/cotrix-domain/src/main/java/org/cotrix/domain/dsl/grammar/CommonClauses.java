package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.traits.Change;

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
	}

	
	public static interface WithManyClause<T,C> {

		/**
		 * Adds one or more parameters to the object.
		 * 
		 * @param parameters the parameters
		 * @return the next clause in the sentence
		 */
		C with(T ... parameters);
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
	
	public static interface VersionClause<T> {

		/**
		 * Sets the version of the object.
		 * 
		 * @param version the version
		 * @return the next clause in the sentence
		 */
		BuildClause<T> version(String version);
	}
	
	public static interface DeltaClause<T> {

		/**
		 * Mark object as an incremental change.
		 * 
		 * @param delta the type of change
		 * @return the next clause in the sentence
		 */
		BuildClause<T> as(Change delta);
	}
	
	public static interface BuildClause<T> extends DeltaClause<T> {

		/**
		 * Closes the sentence and returns the object.
		 * 
		 * @return the object
		 */
		T build();
	}
}
