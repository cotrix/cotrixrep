package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelink.Private;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.trait.Status;

public final class CodelinkMS extends AttributedMS implements Codelink.State {

	private Code.State target;
	private LinkDefinition.State definition;

	public CodelinkMS() {
	}
	
	public CodelinkMS(String id,Status status) {
		super(id,status);
	}
	
	public CodelinkMS(Codelink.State state, Map<String,Object> context) {
		
		//links preserve identifiers
		super(state.id(),state);
		
		notNull("sharing context",context);
		
		target(state.target());
		
		definition(cloneDefinitionInContext(state.definition(),context));
	}
	
	
	//helper
	private LinkDefinition.State cloneDefinitionInContext(LinkDefinition.State def, Map<String,Object> context) {
		
		if (context.containsKey(def.id()))
			return (LinkDefinition.State) context.get(def.id());
			
		LinkDefinition.State clone = new LinkDefinitionMS(def);
		
		context.put(def.id(),clone);
		
		return clone;
		
	}

	@Override
	public QName qname() {
		return definition==null?null:definition.qname();
	}
	
	@Override
	public void qname(QName name) {
		throw new UnsupportedOperationException("codelink names are read-only");
	}
	
	@Override
	public Code.State target() {
		return target;
	}

	@Override
	public void target(Code.State target) {

		notNull("target",target);

		this.target = target;
	}

	@Override
	public LinkDefinition.State definition() {
		
		return definition;
	
	}

	@Override
	public void definition(LinkDefinition.State definition) {
		
		notNull("definition",definition);

		this.definition = definition;
	}
	
	@Override
	public Private entity() {
		return new Codelink.Private(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Codelink.State))
			return false;
		Codelink.State other = (Codelink.State) obj;
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
