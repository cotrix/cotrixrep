package org.cotrix.domain.dsl.builder;

import static org.cotrix.domain.trait.Change.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeStartClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.ThirdClause;
import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.spi.Factory;
import org.cotrix.domain.trait.Change;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributeBuilder implements AttributeStartClause, SecondClause,
										 		  ThirdClause, FinalClause {

	
	private final AttributePO po;
	private final Factory factory;

	
	public AttributeBuilder(Factory factory,String id) {
		this.factory=factory;
		this.po = new AttributePO(id);
	}
	
	@Override
	public SecondClause name(QName name) {
		po.setName(name);
		return this;
	}
	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}

	@Override
	public ThirdClause ofType(QName type) {
		po.setType(type);
		return this;
	}
	
	@Override
	public FinalClause in(String language) {
		po.setLanguage(language);
		return this;
	}

	@Override
	public ThirdClause value(String value) {
		po.setValue(value);
		return this;

	}

	@Override
	public Attribute build() {
		return factory.attribute(po);
	}
	
	@Override
	public FinalClause as(Change change) {
		if (change!=NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setChange(change);
		return this;
	}
}
