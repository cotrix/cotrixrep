package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import java.util.Collection;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Status;

public final class CodelistMS extends VersionedMS implements Codelist.State {

	private NamedStateContainer<Code.State> codes = new NamedStateContainer.Default<Code.State>();

	private NamedStateContainer<CodelistLink.State> links = new NamedStateContainer.Default<CodelistLink.State>();

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
	
	
	public NamedStateContainer<CodelistLink.State> links() {
		return links;
	}

	public void links(Collection<CodelistLink.State> links) {

		notNull("links", links);
		
		for(CodelistLink.State link : links)
			this.links.add(link);
	}

	public void codes(Collection<Code.State> codes) {

		notNull("codes", codes);

		for(Code.State code : codes)
			this.codes.add(code);
	}
	
	@Override
	public NamedStateContainer<Code.State> codes() {
		return codes;
	}
	
	@Override
	public Codelist.Private entity() {
		return new Codelist.Private(this);
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