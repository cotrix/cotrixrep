package org.cotrix.domain.dsl;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeStartClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeStartClause;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.CodebagStartClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistStartClause;
import org.cotrix.domain.simple.SimpleFactory;
import org.cotrix.domain.spi.Factory;
import org.cotrix.domain.traits.Versioned;

/**
 * Model factory.
 * 
 * @author Fabio Simeoni
 *
 */
public class Codes {
	
	private static Factory factory = new SimpleFactory();
	
	/**
	 * Configures the underlying {@link Factory}.
	 * <p>
	 * Typically invoked at application and test entry points.
	 * 
	 * @param factory the factory
	 */
	public static void setFactory(Factory factory) {
		Codes.factory=factory;
	}

	/**
	 * Returns a unqualified name.
	 * @param local the local part of the name
	 * @return the name
	 */
	public static QName q(String local) {
		return new QName(local);
	}
	
	/**
	 * Returns a qualified name.
	 * @param ns the namespace part of the name
	 * @param local the local part of the name
	 * @return
	 */
	public static QName q(String ns, String local) {
		return new QName(ns,local);
	}
	
	/**
	 * Starts a sentence to create an {@link Attribute}.
	 * @return the next clause in the sentence
	 */
	public static AttributeStartClause a() {
		return a(factory.generateId());
	}
	
	/**
	 * Starts a sentence to create an {@link Attribute} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static AttributeStartClause a(String id) {
		return new AttributeBuilder(factory,id);
	}
	
	/**
	 * Returns a {@link Code} with a given unqualified name.
	 * @param name the name
	 * @return the code
	 */
	public static Code ascode(String name) {
		return ascode(q(name));
	}
	
	/**
	 * Returns a {@link Code} with a given qualified name.
	 * @param name the name
	 * @return the code
	 */
	public static Code ascode(QName name) {
		return new CodeBuilder(factory).with(name).build();
	}
	
	/**
	 * Starts a sentence to create an {@link Code}.
	 * @return the next clause in the sentence
	 */
	public static CodeStartClause code() {
		return code(factory.generateId());
	}
	
	/**
	 * Starts a sentence to create an {@link Code} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodeStartClause code(String id) {
		return new CodeBuilder(factory,id);
	}
	
	/**
	 * Starts a sentence to create an {@link Codelist}.
	 * @return the next clause in the sentence
	 */
	public static CodelistStartClause codelist() {
		return codelist(factory.generateId());
	}
	
	/**
	 * Starts a sentence to create an {@link Codelist} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodelistStartClause codelist(String id) {
		return new CodelistBuilder(factory,id);
	}
	
	/**
	 * Starts a sentence to create an {@link Codebag}.
	 * @return the next clause in the sentence
	 */
	public static CodebagStartClause codebag() {
		return codebag(factory.generateId());
	}
	
	/**
	 * Starts a sentence to create an {@link Codebag} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodebagStartClause codebag(String id) {
		return new CodebagBuilder(factory,id);
	}
	
	/**
	 * Starts a sentence to version an object.
	 * @param t the object
	 * @return the next clause in the sentence
	 */
	public static <T extends Versioned<T>> VersionBuilder<T> version(T t) {
		return new VersionBuilder<T>(factory,t);
	}
}