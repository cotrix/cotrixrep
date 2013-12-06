package org.cotrix.domain.memory;

import static java.text.DateFormat.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Status;

public class AttributedMS extends IdentifiedMS implements Attributed.State {

	private Collection<Attribute.State> attributes = new ArrayList<Attribute.State>();

	public AttributedMS() {
		attributes.add(timestamp(CREATION_TIME));
	}
	
	public AttributedMS(String id,Status status) {
		super(id,status);
	}
	
	public AttributedMS(Attributed.State other) {
		for (Attribute.State state : other.attributes())
			attributes.add(new AttributeMS(state));
	}
	
	// helpers
	private Attribute.State timestamp(QName name) {

		AttributeMS state = new AttributeMS();
		state.name(name);
		String value = getDateTimeInstance().format(Calendar.getInstance().getTime());
		state.value(value);
		state.type(SYSTEM_TYPE);
		return state;

	}
	@Override
	public Collection<Attribute.State> attributes() {
		return attributes;
	}
	
	public void attributes(Collection<Attribute.State> attributes) {
		
		notNull("attributes",attributes);
	
		this.attributes.addAll(attributes);
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof AttributedMS))
			return false;
		AttributedMS other = (AttributedMS) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		return true;
	}
		
}
