package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.Collection;
import java.util.Map;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.common.Status;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.trait.Described;


public final class MCode extends MDescribed implements Code.Bean, Described.Bean {

	private BeanContainer<Link.Bean> links = new MBeanContainer<>();

	
	//----------------------------------------------------
	
	
	public MCode() {
	}
	
	public MCode(String id,Status status) {
		super(id,status);
	}

	public MCode(Code.Bean other) {
		
		this(other,null);
	}
	
	public MCode(Code.Bean state,Map<String,Object> defs) {
		
		super(state,defs);
		
		for (Link.Bean link : state.links())
			links.add(new MLink(link,defs));
	}
	
	
	//----------------------------------------------------
	
	public BeanContainer<Link.Bean> links() {
		return links;
	}
	
	
	public void links(Collection<Link.Bean> Codelink) {

		notNull("links", links);
		
		for (Link.Bean link : Codelink)
			this.links.add(link);
	}
	
	@Override
	public Code.Private entity() {
		
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
