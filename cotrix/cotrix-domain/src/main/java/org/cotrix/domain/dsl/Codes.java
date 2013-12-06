package org.cotrix.domain.dsl;

import static org.cotrix.domain.trait.Status.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.dsl.builder.AttributeBuilder;
import org.cotrix.domain.dsl.builder.CodeBuilder;
import org.cotrix.domain.dsl.builder.CodelinkBuilder;
import org.cotrix.domain.dsl.builder.CodelistBuilder;
import org.cotrix.domain.dsl.builder.CodelistLinkBuilder;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeDeltaClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeStartClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeDeltaClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeNewClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkStartClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistNewClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkNewClause;
import org.cotrix.domain.memory.AttributeMS;
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
	
	public static AttributeStartClause attribute() {
		return new AttributeBuilder(new AttributeMS());
	}
	
	public static AttributeDeltaClause modifyAttribute(String id) {
		return new AttributeBuilder(new AttributeMS(id,MODIFIED));
	}
	
	public static Attribute deleteAttribute(String id) {
		return new AttributeMS(id,DELETED).entity();
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
		
	public static CodelinkStartClause codeLink() {
		return new CodelinkBuilder(new CodelinkMS());
	}
	
	public static CodelinkStartClause modifyCodelink(String id) {
		return new CodelinkBuilder(new CodelinkMS(id,MODIFIED));
	}
	

	public static Codelink deleteCodelink(String id) {
		return new CodelinkMS(id,DELETED).entity();
	}

	public static CodelistLinkNewClause listLink() {
		return new CodelistLinkBuilder(new CodelistLinkMS());
	}
	
	public static CodelistLinkNewClause modifyListLink(String id) {
		return new CodelistLinkBuilder(new CodelistLinkMS(id, MODIFIED));
	}
	
	public static CodelistLink deleteListLink(String id) {
		return new CodelistLinkMS(id, DELETED).entity();
	}
	
	//simplifies construction through method parameter inference (not available on constructors in Java 6..)
	
	public static <T extends Identified.Abstract<T,S>, S extends Identified.State & EntityProvider<T>> Container.Private<T, S> container(Collection<S> elements) {
		return new Container.Private<T, S>(elements);
	}
	
	public static <T extends Identified.Abstract<T,S>, S extends Identified.State & EntityProvider<T>> Container.Private<T, S> container(S ... elements) {
		return container(new ArrayList<S>(Arrays.asList(elements)));
	}
	
	public static <T extends Identified.Abstract<T,S> & Named, S extends Identified.State & Named.State & EntityProvider<T>> NamedContainer.Private<T, S> namedContainer(Collection<S> elements) {
		return new NamedContainer.Private<T, S>(elements);
	}
	
	public static <T extends Identified.Abstract<T,S> & Named, S extends Identified.State & Named.State & EntityProvider<T>> NamedContainer.Private<T, S> namedContainer(S ... elements) {
		return namedContainer(new ArrayList<S>(Arrays.asList(elements)));
	}
	
	
}
