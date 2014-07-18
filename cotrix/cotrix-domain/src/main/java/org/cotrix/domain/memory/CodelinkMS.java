package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelink.Private;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.trait.Status;

public final class CodelinkMS extends AttributedMS implements Codelink.State {

	private Code.State target;
	private LinkDefinition.State type;

	public CodelinkMS() {
	}
	
	public CodelinkMS(String id,Status status) {
		super(id,status);
	}
	
	public CodelinkMS(Codelink.State state) {
		
		super(state);
		
		target(state.target());
		
		type(state.type());
	}

	@Override
	public QName name() {
		return type==null?null:type.name();
	}
	
	@Override
	public void name(QName name) {
		throw new UnsupportedOperationException("codelink names are read-only");
	}
	
	public Code.State target() {
		return target;
	}

	public void target(Code.State target) {

		notNull("target",target);

		this.target = target;
	}

	public LinkDefinition.State type() {
		
		return type;
	
	}

	public void type(LinkDefinition.State type) {
		
		notNull("type",type);

		this.type = type;
	}
	
	@Override
	public Private entity() {
		return new Codelink.Private(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Codelink.State))
			return false;
		Codelink.State other = (Codelink.State) obj;
		if (type == null) {
			if (other.type() != null)
				return false;
		} else if (!type.equals(other.type()))
			return false;
		if (target == null) {
			if (other.target() != null)
				return false;
		} else if (!target.equals(other.target()))
			return false;
		return true;
	}
	
	

}
