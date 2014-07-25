package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;


public class NamedMS extends AttributedMS implements Named.State, Attributed.State {

	private QName name;
	
	public NamedMS() {		
	}

	public NamedMS(String id,Status status) {
		super(id,status);
	}
	
	public <T extends Named.State & Attributed.State> NamedMS(T state) {		
		this(state,null);
	}
	
	public <T extends Named.State & Attributed.State> NamedMS(T state,Map<String,Object> context) {		
		super(state,context);
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
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Named.State))
			return false;
		Named.State other = (Named.State) obj;
		if (name == null) {
			if (other.name() != null)
				return false;
		} else if (!name.equals(other.name()))
			return false;
		return true;
	}	
	
	
}
