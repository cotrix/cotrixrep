package org.cotrix.domain.po;

import static java.util.Collections.*;
import static org.cotrix.common.Utils.*;

import java.util.Collection;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;

/**
 * Initialisation parameters for {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public final class CodePO extends NamedPO<Code.Private> implements Code.State {

	private Collection<Codelink.State> links = emptyList();

	/**
	 * Creates an instance with an identifier.
	 * 
	 * @param id the identifier
	 */
	public CodePO(String id) {
		super(id);
	}

	public Collection<Codelink.State> links() {
		return links;
	}

	public void links(Collection<Codelink.State> links) {

		notNull("links", links);
		this.links = links;
	}
}
