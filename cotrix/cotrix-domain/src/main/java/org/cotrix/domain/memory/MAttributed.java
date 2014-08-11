package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.attributes.Facet.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.util.Collection;
import java.util.Map;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.common.Status;
import org.cotrix.domain.trait.Attributed;

public class MAttributed extends MNamed implements Attributed.Bean {

	private BeanContainer<Attribute.Bean> attributes = new MBeanContainer<Attribute.Bean>();

	public MAttributed() {
		
		attributes.add(timestamp());
	
	}
	
	public MAttributed(String id,Status status) {
		super(id,status);
	}

	public MAttributed(Attributed.Bean other,Map<String,Object> context) {
		
		super(other);
		
		attributes.add(timestamp());
		
		for (Attribute.Bean attribute : other.attributes())			
			if (attribute.is(INHERITABLE))
				attributes.add(new MAttribute(attribute,context));				
	}
	
	public MAttributed(Attributed.Bean other) {
		
		this(other,null);			
	}
	
	//create with id
	public MAttributed(String id,Attributed.Bean other,Map<String,Object> context) {
		
		super(id,other);
		
		attributes.add(timestamp());
		
		for (Attribute.Bean attribute : other.attributes())			
			if (attribute.is(INHERITABLE))
				attributes.add(new MAttribute(attribute,context));				
	}

		
		
	public MAttributed(String id,Attributed.Bean other) {
		
		this(id,other,null);			
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
		
	
	//helpers
	private Attribute.Bean timestamp() {
		return beanOf(attribute().instanceOf(CREATED).value(time()));
	}
}
