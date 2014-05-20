package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.links.NameLink.*;
import static org.cotrix.domain.links.OccurrenceRanges.*;
import static org.cotrix.domain.links.ValueFunctions.*;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.codelist.CodelistLink.Private;
import org.cotrix.domain.links.OccurrenceRange;
import org.cotrix.domain.links.ValueFunction;
import org.cotrix.domain.links.LinkValueType;
import org.cotrix.domain.trait.Status;

/**
 * Initialisation parameters for {@link CodelistLink}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkMS extends NamedMS implements CodelistLink.State {

	private Codelist.State target;
	private LinkValueType type;
	private ValueFunction function;
	private OccurrenceRange range;
	
	public CodelistLinkMS() {
		valueType(INSTANCE);
		function(identity);
		range(arbitrarily);
		
	}
	
	public CodelistLinkMS(String id,Status status) {
		super(id,status);
	}
	
	public CodelistLinkMS(CodelistLink.State state) {
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
	
	public OccurrenceRange range() {
		return range;
	}
	
	public void range(OccurrenceRange range) {
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
	
	public Codelist.State target() {
		return target;
	}
	
	public void target(Codelist.State list) {
		notNull("list",list);
		this.target=list;
	}
	
	@Override
	public Private entity() {
		return new CodelistLink.Private(this);
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
		if (!(obj instanceof CodelistLink.State))
			return false;
		CodelistLink.State other = (CodelistLink.State) obj;
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
