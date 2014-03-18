package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.codelist.Codelink.Private;
import org.cotrix.domain.trait.Status;

public final class CodelinkMS extends AttributedMS implements Codelink.State {

	private String targetId;
	private CodelistLink.State definition;

	public CodelinkMS() {
	}
	
	public CodelinkMS(String id,Status status) {
		super(id,status);
	}
	
	public CodelinkMS(Codelink.State state) {
		super(state);
		target(state.target());
		definition(new CodelistLinkMS(state.type()));
	}

	public String target() {
		return targetId;
	}

	public void target(String id) {

		notNull("id",id);

		this.targetId = id;
	}

	public CodelistLink.State type() {
		return definition;
	}

	public void definition(CodelistLink.State definition) {
		
		notNull("definition",definition);

		this.definition = definition;
	}
	
	public void definition(CodelistLink definition) {
		
		notNull("definition",definition);

		definition(reveal(definition,CodelistLink.Private.class));
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
		if (definition == null) {
			if (other.type() != null)
				return false;
		} else if (!definition.equals(other.type()))
			return false;
		if (targetId == null) {
			if (other.target() != null)
				return false;
		} else if (!targetId.equals(other.target()))
			return false;
		return true;
	}
	
	

}
