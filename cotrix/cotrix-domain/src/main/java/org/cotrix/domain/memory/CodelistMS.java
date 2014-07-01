package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Utils.*;

import java.util.Collection;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.attributes.Definition.State;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.trait.Versioned;

public final class CodelistMS extends VersionedMS implements Codelist.State {

	private NamedStateContainer<Code.State> codes = new NamedStateContainer.Default<Code.State>();

	private NamedStateContainer<CodelistLink.State> links = new NamedStateContainer.Default<CodelistLink.State>();

	private NamedStateContainer<Definition.State> defs = new NamedStateContainer.Default<Definition.State>();
	
	public CodelistMS() {
	}
	
	public CodelistMS(String id,Status status) {
		super(id,status);
	}

	public CodelistMS(Codelist.State state) {
		
		super(state);
		
		for (Definition.State atype : state.definitions()) {
			defs.add(new DefinitionMS(atype));
		}
		
		for (CodelistLink.State link : state.links()) {
			links.add(new CodelistLinkMS(link));
		}
		
		
		for (Code.State code : state.codes()) {
			Code.State copy = new CodeMS(code);
			copy.attributes().add(nameof(code));
			copy.attributes().add(idof(code));
			codes.add(copy);
		}
		
		
		attributes().add(versionof(state));
		attributes().add(nameof(state));
		attributes().add(idof(state));
	}
	
	
	@Override
	public NamedStateContainer<State> definitions() {
		return defs;
	}
	
	public NamedStateContainer<CodelistLink.State> links() {
		return links;
	}

	public void definitions(Collection<Definition.State> types) {

		notNull("attribute types", types);
		
		for(Definition.State type : types)
			this.defs.add(type);
	}
	
	public void links(Collection<CodelistLink.State> links) {

		notNull("links", links);
		
		for(CodelistLink.State link : links)
			this.links.add(link);
	}

	public void codes(Collection<Code.State> codes) {

		notNull("codes", codes);

		for(Code.State code : codes)
			this.codes.add(code);
	}
	
	@Override
	public NamedStateContainer<Code.State> codes() {
		return codes;
	}
	
	@Override
	public Codelist.Private entity() {
		return new Codelist.Private(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Codelist.State))
			return false;
		Codelist.State other = (Codelist.State) obj;
		if (codes == null) {
			if (other.codes() != null)
				return false;
		} else if (!codes.equals(other.codes()))
			return false;
		if (links == null) {
			if (other.links() != null)
				return false;
		} else if (!links.equals(other.links()))
			return false;
		return true;
	}
	
	//helpers
	
	private Attribute.State nameof(Named.State named) {
		
		return stateof(attribute().with(PREVIOUS_VERSION_NAME).value(named.name().toString()).build());
	}
	
	private Attribute.State idof(Identified.State identified) {
		
		return stateof(attribute().with(PREVIOUS_VERSION_ID).value(identified.id()).build());
	}
	
	private Attribute.State versionof(Versioned.State versioned) {
		
		return stateof(attribute().with(PREVIOUS_VERSION).value(versioned.version().value()).build());
	}
}
