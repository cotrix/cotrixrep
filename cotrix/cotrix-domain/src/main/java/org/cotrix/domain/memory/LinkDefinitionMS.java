package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.common.Ranges.*;
import static org.cotrix.domain.links.NameLink.*;
import static org.cotrix.domain.values.ValueFunctions.*;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.links.LinkValueType;
import org.cotrix.domain.links.LinkDefinition.Private;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.values.ValueFunction;

/**
 * Initialisation parameters for {@link LinkDefinition}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class LinkDefinitionMS extends NamedMS implements LinkDefinition.State {

	private Codelist.Bean target;
	private LinkValueType type;
	private ValueFunction function;
	private Range range;
	
	public LinkDefinitionMS() {
		valueType(INSTANCE);
		function(identity);
		range(arbitrarily);
		
	}
	
	public LinkDefinitionMS(String id,Status status) {
		super(id,status);
	}
	
	public LinkDefinitionMS(LinkDefinition.State state) {
		super(state);
		
		target(state.target());
		valueType(state.valueType());
		range(state.range());
		function(state.function());
	}

	public LinkValueType valueType() {
		return type;
	}
	
	public void valueType(LinkValueType type) {
		notNull("link type",type);
		this.type=type;
	}
	
	public Range range() {
		return range;
	}
	
	public void range(Range range) {
		notNull("occurrence range",range);
		this.range=range;
	}
	
	@Override
	public ValueFunction function() {
		return function;
	}
	
	@Override
	public void function(ValueFunction function) {
		notNull("value function",function);
		this.function = function;
	}
	
	public Codelist.Bean target() {
		return target;
	}
	
	public void target(Codelist.Bean list) {
		notNull("list",list);
		this.target=list;
	}
	
	@Override
	public Private entity() {
		return new LinkDefinition.Private(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof LinkDefinition.State))
			return false;
		LinkDefinition.State other = (LinkDefinition.State) obj;
		if (function == null) {
			if (other.function() != null)
				return false;
		} else if (!function.equals(other.function()))
			return false;
		if (range == null) {
			if (other.range() != null)
				return false;
		} else if (!range.equals(other.range()))
			return false;
		if (target == null) {
			if (other.target() != null)
				return false;
		} else if (!target.equals(other.target()))
			return false;
		if (type == null) {
			if (other.valueType() != null)
				return false;
		} else if (!type.equals(other.valueType()))
			return false;
		return true;
	}



	
	
}
