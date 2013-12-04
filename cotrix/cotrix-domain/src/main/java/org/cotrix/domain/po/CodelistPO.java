package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;

/**
 * Initialisation parameters for {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public final class CodelistPO extends VersionedPO implements Codelist.State {

	private Collection<Code.State> codes = new ArrayList<Code.State>();
	private Collection<CodelistLink.State> links = new ArrayList<CodelistLink.State>();

	/**
	 * Creates an instance with an identifier.
	 * 
	 * @param id the identifier
	 */
	public CodelistPO(String id) {
		super(id);
	}

	public Collection<CodelistLink.State> links() {
		return links;
	}

	public void links(Collection<CodelistLink.State> links) {

		notNull("links", links);
		this.links = links;
	}

	/**
	 * Sets the codes parameter.
	 * 
	 * @param codes the codes parameter
	 */
	public void codes(Collection<Code.State> codes) {

		notNull("codes", codes);

		this.codes = codes;
	}
	
	@Override
	public Collection<Code.State> codes() {
		return codes;
	}
	
	
}
