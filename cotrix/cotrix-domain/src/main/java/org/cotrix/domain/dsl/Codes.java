package org.cotrix.domain.dsl;

import static org.cotrix.domain.trait.Status.*;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.common.Utils;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.attributes.Text;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.common.StateContainer;
import org.cotrix.domain.dsl.builder.AttributeBuilder;
import org.cotrix.domain.dsl.builder.DefinitionBuilder;
import org.cotrix.domain.dsl.builder.CodeBuilder;
import org.cotrix.domain.dsl.builder.CodelinkBuilder;
import org.cotrix.domain.dsl.builder.CodelistBuilder;
import org.cotrix.domain.dsl.builder.CodelistLinkBuilder;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeChangeClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeNewClause;
import org.cotrix.domain.dsl.grammar.DefinitionGrammar.AttributeTypeChangeClause;
import org.cotrix.domain.dsl.grammar.DefinitionGrammar.AttributeTypeNewClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeDeltaClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeNewClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkChangeClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkNewClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistNewClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkNewClause;
import org.cotrix.domain.memory.AttributeMS;
import org.cotrix.domain.memory.DefinitionMS;
import org.cotrix.domain.memory.CodeMS;
import org.cotrix.domain.memory.CodelinkMS;
import org.cotrix.domain.memory.CodelistLinkMS;
import org.cotrix.domain.memory.CodelistMS;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;

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
	
	public static Attribute deleteAttribute(String id) {
		return new AttributeMS(id,DELETED).entity();
	}
	
	public static AttributeTypeNewClause definition() {
		return new DefinitionBuilder(new DefinitionMS());
	}
	
	public static AttributeTypeChangeClause modifyDefinition(String id) {
		return new DefinitionBuilder(new DefinitionMS(id,MODIFIED));
	}
	
	public static Definition deleteDefinition(String id) {
		return new DefinitionMS(id,DELETED).entity();
	}
	
	public static Text text() {
		return new Text();
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
	
	public static CodeDeltaClause modifyCode(String id) {
		return new CodeBuilder(new CodeMS(id,MODIFIED));
	}
	
	public static Code deleteCode(String id) {
		return new CodeMS(id,DELETED).entity();
	}
	
	
	
	public static CodelistNewClause codelist() {
		return new CodelistBuilder(new CodelistMS());
	}
	
	public static CodelistChangeClause modifyCodelist(String id) {
		return new CodelistBuilder(new CodelistMS(id,MODIFIED));
	}
		
	public static CodelinkNewClause link() {
		return new CodelinkBuilder(new CodelinkMS()).new NewClause();
	}
	
	public static CodelinkChangeClause modifyLink(String id) {
		return new CodelinkBuilder(new CodelinkMS(id,MODIFIED));
	}
	

	public static Codelink deleteLink(String id) {
		return new CodelinkMS(id,DELETED).entity();
	}

	public static CodelistLinkNewClause listLink() {
		return new CodelistLinkBuilder(new CodelistLinkMS()).new NewClause();
	}
	
	public static CodelistLinkChangeClause modifyListLink(String id) {
		return new CodelistLinkBuilder(new CodelistLinkMS(id, MODIFIED)).new ChangeClause();
	}
	
	public static CodelistLink deleteListLink(String id) {
		return new CodelistLinkMS(id, DELETED).entity();
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
	
	public static Attribute.Private reveal(Attribute a) {
		return Utils.reveal(a,Attribute.Private.class);
	}
	
	public static Definition.Private reveal(Definition a) {
		return Utils.reveal(a,Definition.Private.class);
	}
	
	public static Code.Private reveal(Code c) {
		return Utils.reveal(c,Code.Private.class);
	}
	
	public static Codelist.Private reveal(Codelist c) {
		return Utils.reveal(c,Codelist.Private.class);
	}
	
	public static CodelistLink.Private reveal(CodelistLink l) {
		return Utils.reveal(l,CodelistLink.Private.class);
	}
	
	public static Codelink.Private reveal(Codelink l) {
		return Utils.reveal(l,Codelink.Private.class);
	}
}
