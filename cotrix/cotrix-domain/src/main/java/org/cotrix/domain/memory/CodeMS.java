package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.Collection;
import java.util.Map;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Code.Private;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Status;


public final class CodeMS extends NamedMS implements Code.State, Attributed.State {

	private NamedStateContainer<Codelink.State> links = new NamedStateContainer.Default<Codelink.State>();

	public CodeMS() {
	}
	
	public CodeMS(String id,Status status) {
		super(id,status);
	}

	public CodeMS(Code.State state) {
		
		this(state,null);
	}
	
	public CodeMS(Code.State state,Map<String,Object> context) {
		
		super(state,context);
		
		for (Codelink.State link : state.links())
			links.add(new CodelinkMS(link,context));
	}
	
	public NamedStateContainer<Codelink.State> links() {
		return links;
	}
	
	

	public void links(Collection<Codelink.State> Codelink) {

		notNull("links", links);
		
		for (Codelink.State link : Codelink)
			this.links.add(link);
	}
	
	@Override
	public Private entity() {
		
		return new Code.Private(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Code.State))
			return false;
		Code.State other = (Code.State) obj;
		if (links == null) {
			if (other.links() != null)
				return false;
		} else if (!links.equals(other.links()))
			return false;
		return true;
	}
	
	
}
