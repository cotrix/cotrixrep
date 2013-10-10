package org.cotrix.domain.dsl.builder;

import static org.cotrix.domain.trait.Change.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeStartClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.ChangeClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.LanguageClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.TypeClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.ValueClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.DeltaClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.trait.Change;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributeBuilder implements AttributeStartClause, DeltaClause<NameClause<ValueClause>,ChangeClause,Attribute>, ValueClause, ChangeClause,
										 		  TypeClause, LanguageClause {

	
	private final AttributePO po;

	
	public AttributeBuilder(String id) {
		this.po = new AttributePO(id);
	}
	
	@Override
	public ValueClause name(QName name) {
		po.setName(name);
		return this;
	}
	
	@Override
	public ValueClause name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public Attribute delete() {
		return this.as(DELETED).build();
	}
	
	
	@Override
	public NameClause<ValueClause> add() {
		return this.as(NEW);
	}
	
	@Override
	public ChangeClause modify() {
		return this.as(MODIFIED);
	}

	@Override
	public LanguageClause ofType(QName type) {
		po.setType(type);
		return this;
	}
	
	@Override
	public LanguageClause ofType(String type) {
		return ofType(Codes.q(type));
	}
	
	@Override
	public BuildClause<Attribute> in(String language) {
		po.setLanguage(language);
		return this;
	}

	@Override
	public TypeClause value(String value) {
		po.setValue(value);
		return this;

	}

	@Override
	public Attribute build() {
		return new Attribute.Private(po);
	}
	
	private AttributeBuilder as(Change change) {
		if (change!=NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setChange(change);
		return this;
	}
}
