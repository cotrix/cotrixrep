package org.cotrix.domain.dsl.grammar;

import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.CodelistLink;

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
		C attributes(List<Attribute> attributes);
	}
	
	public static interface LinksClause<T,C> {

		/**
		 * Adds one or more links to the object.
		 * 
		 * @param links the links
		 * @return the next clause in the sentence
		 */
		C links(T ... links);
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

	
	public static interface CodeLinkClause<C> {

		/**
		 * Adds a link definition to the object.
		 * 
		 * @param def the definition
		 * @return the next clause in the sentence
		 */
		LinkTargetClause<Code,C> instanceOf(CodelistLink def);
	}
	
	public static interface WithManyClause<T,C> {

		/**
		 * Adds one or more parameters to the object.
		 * 
		 * @param parameters the parameters
		 * @return the next clause in the sentence
		 */
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
	
	
	public static interface DeltaClause<MODIFY,DELETE> {
		
		/**
		 * Marks this object as a modified version of one with the same identifier.
		 * @return the next clause in the sentence
		 */
		MODIFY modify();
		

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
