package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.attributes.AttributeDefinition.State;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.trait.Versioned;

public final class CodelistMS extends VersionedMS implements Codelist.Bean {

	private BeanContainer<Code.Bean> codes = new BeanContainerMS<Code.Bean>();

	private BeanContainer<LinkDefinition.State> links = new BeanContainerMS<LinkDefinition.State>();

	private BeanContainer<AttributeDefinition.State> defs = new BeanContainerMS<AttributeDefinition.State>();
	
	public CodelistMS() {
	}
	
	public CodelistMS(String id,Status status) {
		super(id,status);
	}

	public CodelistMS(Codelist.Bean state) {
		
		super(state);
		
		Map<String,Object> context = new HashMap<>();
		
		for (AttributeDefinition.State def : state.definitions()) {
			AttributeDefinition.State clone = new AttrDefinitionMS(def);
			defs.add(clone);
			context.put(def.id(), clone);
		}
		
		for (LinkDefinition.State def : state.links()) {
			LinkDefinition.State clone = new LinkDefinitionMS(def);
			links.add(clone);
			context.put(def.id(), clone);
		}
		
		for (Code.Bean code : state.codes()) {
			Code.Bean copy = new CodeMS(code,context);
			copy.attributes().add(nameof(code));
			copy.attributes().add(idof(code));
			codes.add(copy);
		}
		
		
		attributes().add(versionof(state));
		attributes().add(nameof(state));
		attributes().add(idof(state));
	}
	
	
	@Override
	public BeanContainer<State> definitions() {
		return defs;
	}
	
	public BeanContainer<LinkDefinition.State> links() {
		return links;
	}

	public void definitions(Collection<AttributeDefinition.State> types) {

		notNull("attribute types", types);
		
		for(AttributeDefinition.State type : types)
			this.defs.add(type);
	}
	
	public void links(Collection<LinkDefinition.State> links) {

		notNull("links", links);
		
		for(LinkDefinition.State link : links)
			this.links.add(link);
	}

	public void codes(Collection<Code.Bean> codes) {

		notNull("codes", codes);

		for(Code.Bean code : codes)
			this.codes.add(code);
	}
	
	@Override
	public BeanContainer<Code.Bean> codes() {
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
		if (!(obj instanceof Codelist.Bean))
			return false;
		Codelist.Bean other = (Codelist.Bean) obj;
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
	
	private Attribute.Bean nameof(Named.Bean named) {
		
		return stateof(attribute().instanceOf(PREVIOUS_VERSION_NAME).value(named.qname().toString()).build());
	}
	
	private Attribute.Bean idof(Identified.Bean identified) {
		
		return stateof(attribute().instanceOf(PREVIOUS_VERSION_ID).value(identified.id()).build());
	}
	
	private Attribute.Bean versionof(Versioned.State versioned) {
		
		return stateof(attribute().instanceOf(PREVIOUS_VERSION).value(versioned.version().value()).build());
	}
}
