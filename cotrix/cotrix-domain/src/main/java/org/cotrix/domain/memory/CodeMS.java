package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.Collection;
import java.util.Map;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Code.Private;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Status;


public final class CodeMS extends NamedMS implements Code.Bean, Attributed.Bean {

	private BeanContainer<Codelink.Bean> links = new BeanContainerMS<Codelink.Bean>();

	public CodeMS() {
	}
	
	public CodeMS(String id,Status status) {
		super(id,status);
	}

	public CodeMS(Code.Bean state) {
		
		this(state,null);
	}
	
	public CodeMS(Code.Bean state,Map<String,Object> context) {
		
		super(state,context);
		
		for (Codelink.Bean link : state.links())
			links.add(new CodelinkMS(link,context));
	}
	
	public BeanContainer<Codelink.Bean> links() {
		return links;
	}
	
	

	public void links(Collection<Codelink.Bean> Codelink) {

		notNull("links", links);
		
		for (Codelink.Bean link : Codelink)
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
		if (!(obj instanceof Code.Bean))
			return false;
		Code.Bean other = (Code.Bean) obj;
		if (links == null) {
			if (other.links() != null)
				return false;
		} else if (!links.equals(other.links()))
			return false;
		return true;
	}
	
	
}
