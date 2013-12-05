package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Code.Private;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Status;


public final class CodeMS extends NamedMS implements Code.State, Attributed.State {

	private Collection<Codelink.State> links = new ArrayList<Codelink.State>();

	public CodeMS() {
	}
	
	public CodeMS(String id,Status status) {
		super(id,status);
	}

	public Collection<Codelink.State> links() {
		return links;
	}
	
	public CodeMS(Code.State state) {
		super(state);
		for (Codelink.State link : state.links())
			links.add(new CodelinkMS(link));
	}

	public void links(Collection<Codelink.State> links) {

		notNull("links", links);
		
		this.links = links;
	}
	
	@Override
	public Private entity() {
		
		return new Code.Private(this);
	}
}
