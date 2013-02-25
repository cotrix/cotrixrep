package org.cotrix.domain.simple;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.pos.AttributePO;
import org.cotrix.domain.pos.CodePO;
import org.cotrix.domain.pos.CodebagPO;
import org.cotrix.domain.pos.CodelistPO;
import org.cotrix.domain.spi.Factory;

/**
 * Default implementation of {@link Factory}.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleFactory implements Factory {

	
	@Override
	public Attribute attribute(AttributePO params) {
		
		return params.language()==null?
				new SimpleAttribute(params):
				new SimpleLanguageAttribute(params);
	}
	
	@Override
	public SimpleCode code(CodePO params) {
		return new SimpleCode(params);
	}
	
	@Override
	public SimpleCodelist codelist(CodelistPO params) {
		return new SimpleCodelist(params);
	}
	
	@Override
	public SimpleCodebag codebag(CodebagPO params) {
		return new SimpleCodebag(params);
	}
	
	@Override
	public String generateId() {
		return null;
	}
}
