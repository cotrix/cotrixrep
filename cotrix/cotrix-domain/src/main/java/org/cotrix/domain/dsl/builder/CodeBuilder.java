package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeDeltaClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeNewClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.SecondClause;
import org.cotrix.domain.po.CodePO;

/**
 * Builds {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodeBuilder implements CodeNewClause, CodeDeltaClause, FinalClause {

	private final CodePO po;
	
	public CodeBuilder() {
		po = new CodePO(null);
	}
	
	public CodeBuilder(String id) {
		po = new CodePO(id);
		po.setChange(MODIFIED);
	}
	
	@Override
	public Code delete() {
		po.setChange(DELETED);
		return build();
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
	public FinalClause links(Codelink... links) {
		po.setLinks(asList(links));
		return this;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		po.setAttributes(Arrays.asList(attributes));
		return this;
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		po.setAttributes(attributes);
		return this;
	}

	
	public Code build() {
		return new Code.Private(po);
	}
	
}
