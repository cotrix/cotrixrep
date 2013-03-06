package org.cotrix.domain.simple;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.po.CodeLinkPO;
import org.cotrix.domain.po.CodePO;
import org.cotrix.domain.po.CodebagPO;
import org.cotrix.domain.po.CodelistLinkPO;
import org.cotrix.domain.po.CodelistPO;
import org.cotrix.domain.primitive.link.CodeLink;
import org.cotrix.domain.primitive.link.CodelistLink;
import org.cotrix.domain.simple.primitive.SimpleCodeLink;
import org.cotrix.domain.simple.primitive.SimpleCodelistLink;
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
	public CodeLink codeLink(CodeLinkPO params) {
		return new SimpleCodeLink(params);
	}
	
	@Override
	public CodelistLink codelistLink(CodelistLinkPO params) {
		return new SimpleCodelistLink(params);
	}
	
	@Override
	public String generateId() {
		return null;
	}
}
