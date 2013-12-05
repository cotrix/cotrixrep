package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.trait.Status;

public final class CodelistMS extends VersionedMS implements Codelist.State {

	private Collection<Code.State> codes = new ArrayList<Code.State>();

	private Collection<CodelistLink.State> links = new ArrayList<CodelistLink.State>();

	public CodelistMS() {
	}
	
	public CodelistMS(String id,Status status) {
		super(id,status);
	}

	public CodelistMS(Codelist.State state) {
		super(state);
		
		for (Code.State code : state.codes())
			codes.add(new CodeMS(code));
		
		for (CodelistLink.State link : state.links())
			links.add(new CodelistLinkMS(link));
	}
	public Collection<CodelistLink.State> links() {
		return links;
	}

	public void links(Collection<CodelistLink.State> links) {

		notNull("links", links);
		
		this.links = links;
	}

	public void codes(Collection<Code.State> codes) {

		notNull("codes", codes);

		this.codes = codes;
	}
	
	@Override
	public Collection<Code.State> codes() {
		return codes;
	}
	
	@Override
	public Codelist.Private entity() {
		return new Codelist.Private(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codes == null) ? 0 : codes.hashCode());
		result = prime * result + ((links == null) ? 0 : links.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof CodelistMS))
			return false;
		CodelistMS other = (CodelistMS) obj;
		if (codes == null) {
			if (other.codes != null)
				return false;
		} else if (!codes.equals(other.codes))
			return false;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
			return false;
		return true;
	}
	
	
}
