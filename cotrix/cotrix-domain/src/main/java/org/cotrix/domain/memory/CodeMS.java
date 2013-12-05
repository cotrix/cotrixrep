package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Code.Private;


public final class CodeMS extends NamedMS implements Code.State {

	private Collection<Codelink.State> links = new ArrayList<Codelink.State>();

	
	public CodeMS(String id) {
		super(id);
	}

	public Collection<Codelink.State> links() {
		return links;
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
