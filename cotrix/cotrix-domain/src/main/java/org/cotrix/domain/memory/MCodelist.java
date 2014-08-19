package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cotrix.common.CommonUtils;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.DefaultVersion;
import org.cotrix.domain.codelist.Version;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.Status;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;

public final class MCodelist extends MDescribed implements Codelist.Bean {

	private Version version;
	
	private Container.Bean<Code.Bean> codes = new MContainer<>();
	
	private Container.Bean<LinkDefinition.Bean> linkDefs = new MContainer<>();
	
	private Container.Bean<AttributeDefinition.Bean> attrDefs = new MContainer<>();
	

	//----------------------------------------------------
	
	public MCodelist() {
		version = new DefaultVersion();
	}
	
	public MCodelist(String id,Status status) {
		super(id,status);
	}

	public MCodelist(Codelist.Bean other) {
		
		super(other);
		
		version(other.version());
		
		Map<String,Object> defs = new HashMap<>();
		
		for (AttributeDefinition.Bean def : other.attributeDefinitions()) {
			AttributeDefinition.Bean clone = new MAttrDef(def);
			attrDefs.add(clone);
			defs.put(def.id(), clone);
		}
		
		for (LinkDefinition.Bean def : other.linkDefinitions()) {
			LinkDefinition.Bean clone = new MLinkDef(def);
			linkDefs.add(clone);
			defs.put(def.id(), clone);
		}
		
		for (Code.Bean code : other.codes()) {
			Code.Bean copy = new MCode(code,defs);
			copy.attributes().add(nameof(code));
			copy.attributes().add(idof(code));
			codes.add(copy);
		}
		
		
		attributes().add(versionof(other));
		attributes().add(nameof(other));
		attributes().add(idof(other));
	}
	
	
	//----------------------------------------------------
	
	public Version version() {
		return version;
	}
	
	@Override
	public void version(Version version) {
		
		CommonUtils.notNull("version",version);
		
		this.version=version;
	}
	
	@Override
	public Container.Bean<AttributeDefinition.Bean> attributeDefinitions() {
		return attrDefs;
	}
	
	public Container.Bean<LinkDefinition.Bean> linkDefinitions() {
		return linkDefs;
	}

	public void attributeDefinitions(Collection<AttributeDefinition.Bean> defs) {

		notNull("attribute definitons", defs);
		
		for(AttributeDefinition.Bean type : defs)
			this.attrDefs.add(type);
	}
	
	public void linkDefinitions(Collection<LinkDefinition.Bean> defs) {

		notNull("link definitions", defs);
		
		for(LinkDefinition.Bean link : defs)
			this.linkDefs.add(link);
	}

	public void codes(Collection<Code.Bean> codes) {

		notNull("codes", codes);

		for(Code.Bean code : codes)
			this.codes.add(code);
	}
	
	@Override
	public Container.Bean<Code.Bean> codes() {
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
		if (version == null) {
			if (other.version() != null)
				return false;
		} else if (!version.equals(other.version()))
			return false;
		if (codes == null) {
			if (other.codes() != null)
				return false;
		} else if (!codes.equals(other.codes()))
			return false;
		if (linkDefs == null) {
			if (other.linkDefinitions() != null)
				return false;
		} else if (!linkDefs.equals(other.linkDefinitions()))
			return false;
		if (attrDefs == null) {
			if (other.linkDefinitions() != null)
				return false;
		} else if (!attrDefs.equals(other.linkDefinitions()))
			return false;
		return true;
	}
	
	//helpers
	
	private Attribute.Bean nameof(Named.Bean named) {
		
		return beanOf(attribute().instanceOf(PREVIOUS_VERSION_NAME).value(named.qname().toString()).build());
	}
	
	private Attribute.Bean idof(Identified.Bean identified) {
		
		return beanOf(attribute().instanceOf(PREVIOUS_VERSION_ID).value(identified.id()).build());
	}
	
	private Attribute.Bean versionof(Codelist.Bean versioned) {
		
		return beanOf(attribute().instanceOf(PREVIOUS_VERSION).value(versioned.version().value()).build());
	}
	
	
}
