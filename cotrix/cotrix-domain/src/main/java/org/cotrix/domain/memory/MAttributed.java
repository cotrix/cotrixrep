package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.Facet.*;

import java.util.Collection;
import java.util.Map;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.common.Status;
import org.cotrix.domain.trait.Attributed;

public class MAttributed extends MNamed implements Attributed.Bean {

	private BeanContainer<Attribute.Bean> attributes = new MBeanContainer<Attribute.Bean>();

	public MAttributed() {	
	}
	
	
	public MAttributed(String id,Status status) {
		super(id,status);
	}

	//copy constructs with shared defs (e.g. codes)
	public MAttributed(Attributed.Bean other,Map<String,Object> definitions) {
		
		super(other);
		
		notNull("shared definitions", definitions);
		
		copy(other,definitions);			
	}
	
	//copy constructs (codelists, linkdefs)
	public MAttributed(Attributed.Bean other) {
		
		super(other);
		
		copy(other);
	}
		
	//copy constructs preserving id (links)
	public MAttributed(String id,Attributed.Bean other) {
		
		super(id,other);	
		
		copy(other);	
	}
	
	
	//copy constructor logic
	private void copy(Attributed.Bean other) {
		
		copy(other,null);	
	}

	//copy constructor logic with shared attributes
	private void copy(Attributed.Bean other,Map<String,Object> defs) {
		
		for (Attribute.Bean otherattr : other.attributes())			
			if (otherattr.is(INHERITABLE))
				attributes.add(defs==null?
						new MAttribute(otherattr):
						new MAttribute(otherattr,defs));	
	}
	
	
	@Override
	public BeanContainer<Attribute.Bean> attributes() {
		return attributes;
	}
	
	public void attributes(Collection<Attribute.Bean> attributes) {
		
		notNull("attributes",attributes);
	
		for (Attribute.Bean a : attributes)
			this.attributes.add(a);
		
	}
	
	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Attributed.Bean))
			return false;
		Attributed.Bean other = (Attributed.Bean) obj;
		if (attributes == null) {
			if (other.attributes() != null)
				return false;
		} else
			if (!attributes.equals(other.attributes()))
				return false;
		
		
		return true;
	}
}
