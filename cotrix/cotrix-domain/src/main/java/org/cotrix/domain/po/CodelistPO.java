package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.primitive.container.Bag;
import org.cotrix.domain.primitive.link.CodelistLink;

/**
 * A set of parameters required to create a {@link Codelist}.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistPO extends VersionedPO {
	
	private Bag<Code> codes = new Bag<Code>(Collections.<Code>emptyList());
	private Bag<CodelistLink> links = new Bag<CodelistLink>(Collections.<CodelistLink>emptyList());	

	/**
	 * Creates an instance with an identifier.
	 * @param id the identifier
	 */
	public CodelistPO(String id) {
		super(id);
	}
	
	/**
	 * Returns the codes parameter.
	 * @return the codes parameter
	 */
	public Bag<Code> codes() {
		return codes;
	}
	
	public Bag<CodelistLink> links() {
		return links;
	}
	
	public void setLinks(Bag<CodelistLink> links) {
		notNull("links",links);
		
		propagateChangeFrom(links);
		
		this.links = links;
	}

	/**
	 * Sets the codes parameter.
	 * @param codes the codes parameter
	 */
	public void setCodes(Bag<Code> codes) {
		
		notNull("codes",codes);
		
		propagateChangeFrom(codes);
		
		this.codes = codes;
	}
}
