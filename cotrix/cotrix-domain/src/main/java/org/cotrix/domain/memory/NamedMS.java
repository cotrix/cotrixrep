package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;


public abstract class NamedMS extends AttributedMS implements Named.State {

	private QName name;
	
	public NamedMS() {		
	}

	public NamedMS(String id,Status status) {
		super(id,status);
	}
	
	public NamedMS(Named.State state) {		
		super(state);
		name(state.name());
	}
	
	public QName name() {
		return name;
	}
	
	public void name(QName name) {
		
		valid("name",name);
		
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof NamedMS))
			return false;
		NamedMS other = (NamedMS) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	
	
	
}
