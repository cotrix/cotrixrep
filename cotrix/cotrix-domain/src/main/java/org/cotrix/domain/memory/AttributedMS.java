package org.cotrix.domain.memory;

import static java.text.DateFormat.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.Calendar;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Status;

public class AttributedMS extends IdentifiedMS implements Attributed.State {

	private NamedStateContainer<Attribute.State> attributes = new NamedStateContainer.Default<Attribute.State>();

	public AttributedMS() {
		attributes.add(timestamp(CREATION_TIME));
	}
	
	public AttributedMS(String id,Status status) {
		super(id,status);
	}
	
	public AttributedMS(Attributed.State other) {
		for (Attribute.State state : other.attributes())
			if (!state.type().equals(SYSTEM_TYPE))
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
		} else if (!attributes.equals(other.attributes()))
			return false;
		return true;
	}
		
}
