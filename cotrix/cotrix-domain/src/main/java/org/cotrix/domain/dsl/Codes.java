package org.cotrix.domain.dsl;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.common.Utils;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.common.StateContainer;
import org.cotrix.domain.dsl.builder.AttributeBuilder;
import org.cotrix.domain.dsl.builder.CodeBuilder;
import org.cotrix.domain.dsl.builder.CodelinkBuilder;
import org.cotrix.domain.dsl.builder.CodelistBuilder;
import org.cotrix.domain.dsl.builder.CodelistLinkBuilder;
import org.cotrix.domain.dsl.builder.DefinitionBuilder;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeChangeClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeNewClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeChangeClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeNewClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkChangeClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkNewClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistNewClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkNewClause;
import org.cotrix.domain.dsl.grammar.DefinitionGrammar.DefinitionChangeClause;
import org.cotrix.domain.dsl.grammar.DefinitionGrammar.DefinitionNewClause;
import org.cotrix.domain.memory.AttributeMS;
import org.cotrix.domain.memory.CodeMS;
import org.cotrix.domain.memory.CodelinkMS;
import org.cotrix.domain.memory.CodelistLinkMS;
import org.cotrix.domain.memory.CodelistMS;
import org.cotrix.domain.memory.DefinitionMS;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.values.DefaultType;

/**
 * Model factory.
 * 
 * @author Fabio Simeoni
 *
 */
public class Codes {
	
	public static QName q(String local) {
		return new QName(local);
	}
	
	public static QName q(String ns, String local) {
		return new QName(ns,local);
	}
	
	public static AttributeNewClause attribute() {
		return new AttributeBuilder(new AttributeMS());
	}
	
	public static AttributeChangeClause modifyAttribute(String id) {
		return new AttributeBuilder(new AttributeMS(id,MODIFIED));
	}
	
	public static AttributeChangeClause modify(Attribute attribute) {
		notNull("attribute",attribute);
		return modifyAttribute(attribute.id());
	}
	
	public static Attribute deleteAttribute(String id) {
		return new AttributeMS(id,DELETED).entity();
	}
	
	public static Attribute delete(Attribute attribute) {
		notNull("attribute",attribute);
		return deleteAttribute(attribute.id());
	}
	
	public static DefinitionNewClause definition() {
		return new DefinitionBuilder(new DefinitionMS());
	}
	
	public static DefinitionChangeClause modifyDefinition(String id) {
		return new DefinitionBuilder(new DefinitionMS(id,MODIFIED));
	}
	
	public static DefinitionChangeClause modify(Definition def) {
		notNull("definition",def);
		return modifyDefinition(def.id());
	}
	
	public static DefaultType valueType() {
		return new DefaultType();
	}
	
	public static Code ascode(String name) {
		return ascode(q(name));
	}
	
	public static Code ascode(QName name) {
		return new CodeBuilder(new CodeMS()).name(name).build();
	}
	
	public static CodeNewClause code() {
		return new CodeBuilder(new CodeMS());
	}
	
	public static CodeChangeClause modifyCode(String id) {
		return new CodeBuilder(new CodeMS(id,MODIFIED));
	}
	
	public static CodeChangeClause modify(Code code) {
		notNull("code",code);
		return modifyCode(code.id());
	}
	
	public static Code deleteCode(String id) {
		return new CodeMS(id,DELETED).entity();
	}
	
	public static Code delete(Code code) {
		notNull("code",code);
		return deleteCode(code.id());
	}
	
	
	
	public static CodelistNewClause codelist() {
		return new CodelistBuilder(new CodelistMS());
	}
	
	public static CodelistChangeClause modifyCodelist(String id) {
		return new CodelistBuilder(new CodelistMS(id,MODIFIED));
	}
	
	public static CodelistChangeClause modify(Codelist list) {
		notNull("codelist",list);
		return modifyCodelist(list.id());
	}
		
	public static CodelinkNewClause link() {
		return new CodelinkBuilder(new CodelinkMS()).new NewClause();
	}
	
	public static CodelinkChangeClause modifyLink(String id) {
		return new CodelinkBuilder(new CodelinkMS(id,MODIFIED));
	}
	
	public static CodelinkChangeClause modify(Codelink link) {
		notNull("code link",link);
		return modifyLink(link.id());
	}

	public static Codelink deleteLink(String id) {
		return new CodelinkMS(id,DELETED).entity();
	}

	public static Codelink delete(Codelink link) {
		notNull("code link",link);
		return deleteLink(link.id());
	}
	
	
	public static CodelistLinkNewClause listLink() {
		return new CodelistLinkBuilder(new CodelistLinkMS()).new NewClause();
	}
	
	public static CodelistLinkChangeClause modifyListLink(String id) {
		return new CodelistLinkBuilder(new CodelistLinkMS(id, MODIFIED)).new ChangeClause();
	}
	
	public static CodelistLinkChangeClause modify(CodelistLink link) {
		notNull("codelist link",link);
		return modifyListLink(link.id());
	}
	
	//simplifies construction through method parameter inference (not available on constructors in Java 6..)
	
	public static <T extends Identified.Abstract<T,S>, S extends Identified.State & EntityProvider<T>> Container.Private<T,S> container(StateContainer<S> elements) {
		return new Container.Private<T,S>(elements);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Identified.Abstract<T,S>, S extends Identified.State & EntityProvider<T>> Container.Private<T, S> container(S ... elements) {
		return container(beans(elements));
	}
	
	public static <T extends Identified.Abstract<T,S> & Named, S extends Identified.State & Named.State & EntityProvider<T>> NamedContainer.Private<T,S> namedContainer(NamedStateContainer<S> elements) {
		return new NamedContainer.Private<T, S>(elements);
	}
	
	public static <S extends Identified.State> StateContainer<S> beans(Collection<S> elements) {
		return new StateContainer.Default<S>(elements);
	}
	
	public static <S extends Identified.State & Named.State> NamedStateContainer<S> namedBeans(Collection<S> elements) {
		return new NamedStateContainer.Default<S>(elements);
	}
	
	@SuppressWarnings("unchecked")
	public static <S extends Identified.State> StateContainer<S> beans(S ... elements) {
		return new StateContainer.Default<S>(Arrays.asList(elements));
	}

	@SuppressWarnings("unchecked")
	public static <S extends Identified.State & Named.State> NamedStateContainer<S> namedBeans(S ... elements) {
		return new NamedStateContainer.Default<S>(Arrays.asList(elements));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Identified.Abstract<T,S> & Named, S extends Identified.State & Named.State & EntityProvider<T>> NamedContainer.Private<T, S> namedContainer(S ... elements) {
		return namedContainer(namedBeans(elements));
	}
	
	public static Attribute.Private reveal(Attribute attribute) {
		notNull("attribute",attribute);
		return Utils.reveal(attribute,Attribute.Private.class);
	}
	
	public static Definition.Private reveal(Definition definition) {
		notNull("definition",definition);
		return Utils.reveal(definition,Definition.Private.class);
	}
	
	public static Code.Private reveal(Code code) {
		notNull("code",code);
		return Utils.reveal(code,Code.Private.class);
	}
	
	public static Codelist.Private reveal(Codelist codelist) {
		notNull("codelist",codelist);
		return Utils.reveal(codelist,Codelist.Private.class);
	}
	
	public static CodelistLink.Private reveal(CodelistLink link) {
		notNull("codelist link",link);
		return Utils.reveal(link,CodelistLink.Private.class);
	}
	
	public static Codelink.Private reveal(Codelink link) {
		notNull("code link",link);
		return Utils.reveal(link,Codelink.Private.class);
	}
}
