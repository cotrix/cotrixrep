package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.codelist.CodelistLink.Private;
import org.cotrix.domain.trait.Status;

/**
 * Initialisation parameters for {@link CodelistLink}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkMS extends NamedMS implements CodelistLink.State {

	private String targetId;
	
	public CodelistLinkMS() {
	}
	
	public CodelistLinkMS(String id,Status status) {
		super(id,status);
	}
	
	public CodelistLinkMS(CodelistLink.State state) {
		super(state);
		image(state.image());
	}

	
	public String image() {
		return targetId;
	}
	
	public void image(String id) {
		notNull("id",id);
		this.targetId=id;
	}
	
	@Override
	public Private entity() {
		return new CodelistLink.Private(this);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((targetId == null) ? 0 : targetId.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof CodelistLink.State))
			return false;
		CodelistLink.State other = (CodelistLink.State) obj;
		if (targetId == null) {
			if (other.image() != null)
				return false;
		} else if (!targetId.equals(other.image()))
			return false;
		return true;
	}
	
	
}
