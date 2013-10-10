package org.cotrix.domain.dsl;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.dsl.builder.AttributeBuilder;
import org.cotrix.domain.dsl.builder.CodeBuilder;
import org.cotrix.domain.dsl.builder.CodeLinkBuilder;
import org.cotrix.domain.dsl.builder.CodebagBuilder;
import org.cotrix.domain.dsl.builder.CodelistBuilder;
import org.cotrix.domain.dsl.builder.CodelistLinkBuilder;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeStartClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeStartClause;
import org.cotrix.domain.dsl.grammar.CodeLinkGrammar.CodeLinkStartClause;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.CodebagStartClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistStartClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkStartClause;

/**
 * Model factory.
 * 
 * @author Fabio Simeoni
 *
 */
public class Codes {
	
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
	public static AttributeStartClause attr() {
		return attr(null);
	}
	
	/**
	 * Starts a sentence to create an {@link Attribute} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static AttributeStartClause attr(String id) {
		return new AttributeBuilder(id);
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
		return new CodeBuilder(null).name(name).build();
	}
	
	/**
	 * Starts a sentence to create an {@link Code}.
	 * @return the next clause in the sentence
	 */
	public static CodeStartClause code() {
		return code(null);
	}
	
	/**
	 * Starts a sentence to create an {@link Code} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodeStartClause code(String id) {
		return new CodeBuilder(id);
	}
	
	/**
	 * Starts a sentence to create an {@link Codelink} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodeLinkStartClause codeLink(String id) {
		return new CodeLinkBuilder(id);
	}
	
	/**
	 * Starts a sentence to create an {@link Codelink} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodeLinkStartClause codeLink() {
		return codeLink(null);
	}
	
	/**
	 * Starts a sentence to create an {@link Codelist}.
	 * @return the next clause in the sentence
	 */
	public static CodelistStartClause codelist() {
		return codelist(null);
	}
	
	/**
	 * Starts a sentence to create an {@link Codelist} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodelistStartClause codelist(String id) {
		return new CodelistBuilder(id);
	}
	
	/**
	 * Starts a sentence to create an {@link CodelistLink} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodelistLinkStartClause listLink(String id) {
		return new CodelistLinkBuilder(id);
	}
	
	/**
	 * Starts a sentence to create an {@link CodelistLink} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodelistLinkStartClause listLink() {
		return listLink(null);
	}
	
	/**
	 * Starts a sentence to create an {@link Codebag}.
	 * @return the next clause in the sentence
	 */
	public static CodebagStartClause codebag() {
		return codebag(null);
	}
	
	/**
	 * Starts a sentence to create an {@link Codebag} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodebagStartClause codebag(String id) {
		return new CodebagBuilder(id);
	}
}
