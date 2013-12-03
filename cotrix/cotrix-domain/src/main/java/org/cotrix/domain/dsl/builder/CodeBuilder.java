package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.common.Attribute;
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
		po.change(MODIFIED);
	}
	
	@Override
	public Code delete() {
		po.change(DELETED);
		return build();
	}
	
	@Override
	public SecondClause name(QName name) {
		po.name(name);
		return this;
	}

	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}

	@Override
	public FinalClause links(Codelink... links) {
		
		List<Codelink.State> state = new ArrayList<Codelink.State>();
		
		for (Codelink a : links)
			state.add(reveal(a,Codelink.Private.class).state());
		
		po.links(state);
		
		return this;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		return attributes(Arrays.asList(attributes));
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		
		List<Attribute.State> state = new ArrayList<Attribute.State>();
		
		for (Attribute a : attributes)
			state.add(reveal(a,Attribute.Private.class).state());
		
		po.attributes(state);
		return this;
	}

	
	public Code build() {
		return new Code.Private(po);
	}
	
}
