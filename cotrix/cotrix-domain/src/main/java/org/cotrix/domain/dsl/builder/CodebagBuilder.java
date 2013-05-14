package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.trait.Change.*;

import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.CodebagStartClause;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.ThirdClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.po.CodebagPO;
import org.cotrix.domain.trait.Change;
import org.cotrix.domain.version.SimpleVersion;
import org.cotrix.domain.version.Version;

/**
 * Builds {@link Codebag}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodebagBuilder implements CodebagStartClause, SecondClause,ThirdClause,FinalClause {

	private final CodebagPO po;
	
	public CodebagBuilder(String id) {
		po = new CodebagPO(id);
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
	public ThirdClause with(List<Codelist> lists) {
		po.setLists(lists);
		return this;
	}
	
	@Override
	public ThirdClause with(Codelist ... lists) {
		po.setLists(asList(lists));
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
	
	public CodebagBuilder version(Version version) {
		po.setVersion(version);
		return this;
	}
	
	public CodebagBuilder version(String version) {
		po.setVersion(new SimpleVersion(version));
		return this;
	}
	
	@Override
	public BuildClause<Codebag> as(Change change) {
		if (change!=NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setChange(change);
		return this;
	}
	
	public Codebag build() {
		return new Codebag.Private(po);
	}
	
}
