package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.codelist.CodelistLink.Private;

/**
 * Initialisation parameters for {@link CodelistLink}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkMS extends NamedMS implements CodelistLink.State {

	private String targetId;
	
	public CodelistLinkMS(String id) {
		super(id);
	}

	
	public String targetId() {
		return targetId;
	}
	
	public void targetId(String id) {
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
		if (!(obj instanceof CodelistLinkMS))
			return false;
		CodelistLinkMS other = (CodelistLinkMS) obj;
		if (targetId == null) {
			if (other.targetId != null)
				return false;
		} else if (!targetId.equals(other.targetId))
			return false;
		return true;
	}
	
	
}
