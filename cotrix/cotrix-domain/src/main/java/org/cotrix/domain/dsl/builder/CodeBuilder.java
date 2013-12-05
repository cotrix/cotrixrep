package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;
import static org.cotrix.domain.trait.Status.*;

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
import org.cotrix.domain.memory.CodeMS;

/**
 * Builds {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodeBuilder implements CodeNewClause, CodeDeltaClause, FinalClause {

	private final CodeMS state;
	
	public CodeBuilder() {
		state = new CodeMS(null);
	}
	
	public CodeBuilder(String id) {
		state = new CodeMS(id);
		state.status(MODIFIED);
	}
	
	@Override
	public Code delete() {
		state.status(DELETED);
		return build();
	}
	
	@Override
	public SecondClause name(QName name) {
		state.name(name);
		return this;
	}

	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}

	@Override
	public FinalClause links(Codelink... links) {
		
		state.links(reveal(asList(links),Codelink.Private.class));
		
		return this;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		return attributes(Arrays.asList(attributes));
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		
		state.attributes(reveal(attributes,Attribute.Private.class));
		
		return this;
	}

	
	public Code build() {
		return state.entity();
	}
	
}
