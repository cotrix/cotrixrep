package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.primitive.container.MutableContainer;
import org.cotrix.domain.primitive.link.CodelistLink;

/**
 * Initialisation parameters for {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistPO extends VersionedPO {
	
	private MutableContainer<Code> codes = new MutableContainer<Code>(Collections.<Code>emptyList());
	private MutableContainer<CodelistLink> links = new MutableContainer<CodelistLink>(Collections.<CodelistLink>emptyList());	

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
	public MutableContainer<Code> codes() {
		return codes;
	}
	
	public MutableContainer<CodelistLink> links() {
		return links;
	}
	
	public void setLinks(MutableContainer<CodelistLink> links) {
		notNull("links",links);
		
		propagateChangeFrom(links);
		
		this.links = links;
	}

	/**
	 * Sets the codes parameter.
	 * @param codes the codes parameter
	 */
	public void setCodes(MutableContainer<Code> codes) {
		
		notNull("codes",codes);
		
		propagateChangeFrom(codes);
		
		this.codes = codes;
	}
}
