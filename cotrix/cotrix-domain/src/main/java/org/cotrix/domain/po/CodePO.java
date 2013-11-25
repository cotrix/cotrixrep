package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import java.util.Collections;
import java.util.List;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.common.Container;

/**
 * Initialisation parameters for {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public final class CodePO extends NamedPO {

	private Container.Private<Codelink.Private> links = new Container.Private<Codelink.Private>(
			Collections.<Codelink.Private> emptyList());

	/**
	 * Creates an instance with an identifier.
	 * 
	 * @param id the identifier
	 */
	public CodePO(String id) {
		super(id);
	}

	public Container.Private<Codelink.Private> links() {
		return links;
	}

	public void setLinks(List<Codelink> links) {

		Container.Private<Codelink.Private> privateLinks = new Container.Private<Codelink.Private>(reveal(links,
				Codelink.Private.class));
		this.setLinks(privateLinks);
	}

	public void setLinks(Container.Private<Codelink.Private> links) {
		notNull("links", links);
		this.links = links;
	}
}
