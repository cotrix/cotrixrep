package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import java.util.Collections;
import java.util.List;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Container;

/**
 * Initialisation parameters for {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public final class CodelistPO extends VersionedPO {

	private Container.Private<Code.Private> codes = new Container.Private<Code.Private>(
			Collections.<Code.Private> emptyList());
	private Container.Private<CodelistLink.Private> links = new Container.Private<CodelistLink.Private>(
			Collections.<CodelistLink.Private> emptyList());

	/**
	 * Creates an instance with an identifier.
	 * 
	 * @param id the identifier
	 */
	public CodelistPO(String id) {
		super(id);
	}

	/**
	 * Returns the codes parameter.
	 * 
	 * @return the codes parameter
	 */
	public Container.Private<Code.Private> codes() {
		return codes;
	}

	public Container.Private<CodelistLink.Private> links() {
		return links;
	}

	public void setLinks(List<CodelistLink> links) {

		Container.Private<CodelistLink.Private> privateLinks = new Container.Private<CodelistLink.Private>(reveal(links,
				CodelistLink.Private.class));
		this.setLinks(privateLinks);
	}

	public void setLinks(Container.Private<CodelistLink.Private> links) {

		notNull("links", links);

		this.links = links;
	}

	/**
	 * Sets the codes parameter.
	 * 
	 * @param codes the codes parameter
	 */
	public void setCodes(Container.Private<Code.Private> codes) {

		notNull("codes", codes);

		this.codes = codes;
	}

	/**
	 * Sets the codes parameter.
	 * 
	 * @param codes the codes parameter
	 */
	public void setCodes(List<Code> codes) {

		Container.Private<Code.Private> privateCodes = new Container.Private<Code.Private>(reveal(codes, Code.Private.class));
		this.setCodes(privateCodes);
	}
}
