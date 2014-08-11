package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Link;
import org.cotrix.domain.common.Status;
import org.cotrix.domain.links.LinkDefinition;

public final class MLink extends MAttributed implements Link.Bean {

	private Code.Bean target;
	private LinkDefinition.Bean definition;

	public MLink() {
	}
	
	public MLink(String id,Status status) {
		super(id,status);
	}
	
	public MLink(Link.Bean other, Map<String,Object> context) {
		
		//links preserve identifiers
		super(other.id(),other);
		
		notNull("sharing context",context);
		
		target(other.target());
		
		definition(cloneDefinitionInContext(other.definition(),context));
	}
	
	
	//helper
	private LinkDefinition.Bean cloneDefinitionInContext(LinkDefinition.Bean def, Map<String,Object> context) {
		
		if (context==null || !context.containsKey(def.id()))
			throw new AssertionError("application error: definition cannot be shared during copy");
		
		return (LinkDefinition.Bean) context.get(def.id());
			
	}

	@Override
	public QName qname() {
		return definition==null?null:definition.qname();
	}
	
	@Override
	public void qname(QName name) {
		//overrides super to avoid adding a property that should remain virtual
	}
	
	@Override
	public Code.Bean target() {
		return target;
	}

	@Override
	public void target(Code.Bean target) {

		notNull("target",target);

		this.target = target;
	}

	@Override
	public LinkDefinition.Bean definition() {
		
		return definition;
	
	}

	@Override
	public void definition(LinkDefinition.Bean definition) {
		
		notNull("definition",definition);

		this.definition = definition;
	}
	
	@Override
	public Link.Private entity() {
		return new Link.Private(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Link.Bean))
			return false;
		Link.Bean other = (Link.Bean) obj;
		if (definition == null) {
			if (other.definition() != null)
				return false;
		} else if (!definition.equals(other.definition()))
			return false;
		if (target == null) {
			if (other.target() != null)
				return false;
		} else if (!target.equals(other.target()))
			return false;
		return true;
	}
	
	

}
