package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.codelist.Codelist;
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

	private Codelist target;
	
	public CodelistLinkMS() {
	}
	
	public CodelistLinkMS(String id,Status status) {
		super(id,status);
	}
	
	public CodelistLinkMS(CodelistLink.State state) {
		super(state);
		target(state.target());
	}

	
	public Codelist target() {
		return target;
	}
	
	public void target(Codelist list) {
		notNull("list",list);
		this.target=list;
	}
	
	@Override
	public Private entity() {
		return new CodelistLink.Private(this);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		if (target == null) {
			if (other.target() != null)
				return false;
		} else if (!target.equals(other.target()))
			return false;
		return true;
	}
	
	
}
