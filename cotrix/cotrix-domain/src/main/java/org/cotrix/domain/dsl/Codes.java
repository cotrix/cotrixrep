package org.cotrix.domain.dsl;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.common.Status.*;

import javax.xml.namespace.QName;

import org.cotrix.common.CommonUtils;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.builder.AttributeBuilder;
import org.cotrix.domain.dsl.builder.CodeBuilder;
import org.cotrix.domain.dsl.builder.CodelinkBuilder;
import org.cotrix.domain.dsl.builder.CodelistBuilder;
import org.cotrix.domain.dsl.builder.DefinitionBuilder;
import org.cotrix.domain.dsl.builder.LinkDefinitionBuilder;
import org.cotrix.domain.dsl.grammar.AttributeDefinitionGrammar.AttributeDefinitionChangeClause;
import org.cotrix.domain.dsl.grammar.AttributeDefinitionGrammar.AttributeDefinitionNewClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeChangeClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeNewClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeChangeClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeNewClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkChangeClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkNewClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistNewClause;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.LinkDefinitionChangeClause;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.LinkDefinitionNewClause;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.MAttrDef;
import org.cotrix.domain.memory.MAttribute;
import org.cotrix.domain.memory.MCode;
import org.cotrix.domain.memory.MCodelist;
import org.cotrix.domain.memory.MLink;
import org.cotrix.domain.memory.MLinkDef;
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
		return new AttributeBuilder(new MAttribute());
	}
	
	public static AttributeChangeClause modifyAttribute(String id) {
		return new AttributeBuilder(new MAttribute(id,MODIFIED));
	}
	
	public static AttributeChangeClause modify(Attribute attribute) {
		notNull("attribute",attribute);
		return modifyAttribute(attribute.id());
	}
	
	public static Attribute deleteAttribute(String id) {
		return new MAttribute(id,DELETED).entity();
	}
	
	public static Attribute delete(Attribute attribute) {
		notNull("attribute",attribute);
		return deleteAttribute(attribute.id());
	}
	
	public static AttributeDefinitionNewClause attrdef() {
		return new DefinitionBuilder(new MAttrDef());
	}
	
	public static AttributeDefinitionChangeClause modifyAttrDef(String id) {
		return new DefinitionBuilder(new MAttrDef(id,MODIFIED));
	}
	
	public static AttributeDefinitionChangeClause modify(AttributeDefinition def) {
		notNull("definition",def);
		return modifyAttrDef(def.id());
	}
	
	public static DefaultType valueType() {
		return new DefaultType();
	}
	
	public static Code ascode(String name) {
		return ascode(q(name));
	}
	
	public static Code ascode(QName name) {
		return new CodeBuilder(new MCode()).name(name).build();
	}
	
	public static CodeNewClause code() {
		return new CodeBuilder(new MCode());
	}
	
	public static CodeChangeClause modifyCode(String id) {
		return new CodeBuilder(new MCode(id,MODIFIED));
	}
	
	public static CodeChangeClause modify(Code code) {
		notNull("code",code);
		return modifyCode(code.id());
	}
	
	public static Code deleteCode(String id) {
		return new MCode(id,DELETED).entity();
	}
	
	public static Code delete(Code code) {
		notNull("code",code);
		return deleteCode(code.id());
	}
	
	
	
	public static CodelistNewClause codelist() {
		return new CodelistBuilder(new MCodelist());
	}
	
	public static CodelistChangeClause modifyCodelist(String id) {
		return new CodelistBuilder(new MCodelist(id,MODIFIED));
	}
	
	public static CodelistChangeClause modify(Codelist list) {
		notNull("codelist",list);
		return modifyCodelist(list.id());
	}
		
	public static CodelinkNewClause link() {
		return new CodelinkBuilder(new MLink()).new NewClause();
	}
	
	public static CodelinkChangeClause modifyLink(String id) {
		return new CodelinkBuilder(new MLink(id,MODIFIED));
	}
	
	public static CodelinkChangeClause modify(Link link) {
		notNull("code link",link);
		return modifyLink(link.id());
	}

	public static Link deleteLink(String id) {
		return new MLink(id,DELETED).entity();
	}

	public static Link delete(Link link) {
		notNull("code link",link);
		return deleteLink(link.id());
	}
	
	
	public static LinkDefinitionNewClause linkdef() {
		return new LinkDefinitionBuilder(new MLinkDef()).new NewClause();
	}
	
	public static LinkDefinitionChangeClause modifyLinkDef(String id) {
		return new LinkDefinitionBuilder(new MLinkDef(id, MODIFIED)).new ChangeClause();
	}
	
	public static LinkDefinitionChangeClause modify(LinkDefinition link) {
		notNull("link definition",link);
		return modifyLinkDef(link.id());
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	
	public static Attribute.Private reveal(Attribute attribute) {
		notNull("attribute",attribute);
		return CommonUtils.reveal(attribute,Attribute.Private.class);
	}
	
	public static AttributeDefinition.Private reveal(AttributeDefinition definition) {
		notNull("definition",definition);
		return CommonUtils.reveal(definition,AttributeDefinition.Private.class);
	}
	
	public static Code.Private reveal(Code code) {
		notNull("code",code);
		return CommonUtils.reveal(code,Code.Private.class);
	}
	
	public static Codelist.Private reveal(Codelist codelist) {
		notNull("codelist",codelist);
		return CommonUtils.reveal(codelist,Codelist.Private.class);
	}
	
	public static LinkDefinition.Private reveal(LinkDefinition link) {
		notNull("codelist link",link);
		return CommonUtils.reveal(link,LinkDefinition.Private.class);
	}
	
	public static Link.Private reveal(Link link) {
		notNull("code link",link);
		return CommonUtils.reveal(link,Link.Private.class);
	}
	
}
