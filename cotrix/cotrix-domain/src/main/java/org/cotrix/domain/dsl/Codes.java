package org.cotrix.domain.dsl;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container.Private;
import org.cotrix.domain.dsl.builder.AttributeBuilder;
import org.cotrix.domain.dsl.builder.CodeBuilder;
import org.cotrix.domain.dsl.builder.CodeLinkBuilder;
import org.cotrix.domain.dsl.builder.CodelistBuilder;
import org.cotrix.domain.dsl.builder.CodelistLinkBuilder;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeDeltaClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeStartClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeDeltaClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeNewClause;
import org.cotrix.domain.dsl.grammar.CodeLinkGrammar.CodeLinkStartClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistNewClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkNewClause;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.EntityProvider;

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
		return new AttributeBuilder();
	}
	
	/**
	 * Starts a sentence to create an {@link Attribute} changeset.
	 * @param id the identifier of the target attribute
	 * @return the next clause in the sentence
	 */
	public static AttributeDeltaClause attr(String id) {
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
		return new CodeBuilder().name(name).build();
	}
	
	/**
	 * Starts a sentence to create an {@link Code}.
	 * @return the next clause in the sentence
	 */
	public static CodeNewClause code() {
		return new CodeBuilder();
	}
	
	/**
	 * Starts a sentence to create a {@link Code} changeset.
	 * @param id the identifier of the target code
	 * @return the next clause in the sentence
	 */
	public static CodeDeltaClause code(String id) {
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
		return new CodeLinkBuilder();
	}
	
	/**
	 * Starts a sentence to create a {@link Codelist}.
	 * @return the next clause in the sentence
	 */
	public static CodelistNewClause codelist() {
		return new CodelistBuilder();
	}
	
	/**
	 * Starts a sentence to create an {@link Codelist} changeset.
	 * @param id the identifier of the target codelist
	 * @return the next clause in the sentence
	 */
	public static CodelistChangeClause codelist(String id) {
		return new CodelistBuilder(id);
	}

	/**
	 * Starts a sentence to create an {@link CodelistLink} with a given identifier.
	 * @param id the identifier
	 * @return the next clause in the sentence
	 */
	public static CodelistLinkNewClause listLink() {
		return new CodelistLinkBuilder();
	}
	
	/**
	 * Starts a sentence to create a {@link CodelistLink} changeset.
	 * @param id the identifier of the target link
	 * @return the next clause in the sentence
	 */
	public static CodelistLinkChangeClause listLink(String id) {
		return new CodelistLinkBuilder(id);
	}
	
	//simplifies construction through method parameter inference (not available on constructors in Java 6..)
	public static <T extends Identified.Abstract<T,S>, S extends Identified.State & EntityProvider<T>> Private<T,S> container(Collection<S> elements) {
		return new Private<T, S>(elements);
	}
	
	
}
