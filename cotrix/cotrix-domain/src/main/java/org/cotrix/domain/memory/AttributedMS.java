package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.attributes.Facet.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.util.Collection;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Status;

public class AttributedMS extends IdentifiedMS implements Attributed.State {

	private NamedStateContainer<Attribute.State> attributes = new NamedStateContainer.Default<Attribute.State>();

	public AttributedMS() {
		
		attributes.add(timestamp());
	
	}
	
	public AttributedMS(String id,Status status) {
		super(id,status);
	}
	
	public AttributedMS(Attributed.State other) {
		
		this();
		
		for (Attribute.State attribute : other.attributes())			
			if (attribute.is(INHERITABLE))
				attributes.add(new AttributeMS(attribute));				
	}
	
	@Override
	public NamedStateContainer<Attribute.State> attributes() {
		return attributes;
	}
	
	public void attributes(Collection<Attribute.State> attributes) {
		
		notNull("attributes",attributes);
	
		for (Attribute.State a : attributes)
			this.attributes.add(a);
		
	}
	
	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Attributed.State))
			return false;
		Attributed.State other = (Attributed.State) obj;
		if (attributes == null) {
			if (other.attributes() != null)
				return false;
		} else
			if (!attributes.equals(other.attributes()))
				return false;
		
		
		return true;
	}
		
	
	//helpers
	private Attribute.State timestamp() {
		return stateof(attribute().with(CREATION_TIME).value(time()));
	}
}
