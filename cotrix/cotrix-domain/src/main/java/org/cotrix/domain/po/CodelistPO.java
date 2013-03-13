package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;
import java.util.List;

import org.cotrix.domain.Code;
import org.cotrix.domain.Code.Private;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.primitive.PContainer;

/**
 * Initialisation parameters for {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistPO extends VersionedPO {
	
	private PContainer<Code.Private> codes = new PContainer<Private>(Collections.<Private>emptyList());
	private PContainer<CodelistLink.Private> links = new PContainer<CodelistLink.Private>(Collections.<CodelistLink.Private>emptyList());	

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
	public PContainer<Code.Private> codes() {
		return codes;
	}
	
	public PContainer<CodelistLink.Private> links() {
		return links;
	}
	
	public void setLinks(List<CodelistLink> links) {
		
		PContainer<CodelistLink.Private> privateLinks = new PContainer<CodelistLink.Private>(reveal(links,CodelistLink.Private.class));
		this.setLinks(privateLinks);
	}
	
	public void setLinks(PContainer<CodelistLink.Private> links) {
		
		notNull("links",links);
		
		propagateChangeFrom(links);
		
		this.links = links;
	}

	/**
	 * Sets the codes parameter.
	 * @param codes the codes parameter
	 */
	public void setCodes(PContainer<Code.Private> codes) {
		
		notNull("codes",codes);
		
		propagateChangeFrom(codes);
		
		this.codes = codes;
	}
	
	/**
	 * Sets the codes parameter.
	 * @param codes the codes parameter
	 */
	public void setCodes(List<Code> codes) {
		
		PContainer<Code.Private> privateCodes = new PContainer<Code.Private>(reveal(codes,Code.Private.class));
		this.setCodes(privateCodes);
	}
}
