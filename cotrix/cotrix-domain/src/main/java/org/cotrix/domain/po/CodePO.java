package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;
import java.util.List;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.Codelink.Private;
import org.cotrix.domain.primitive.PContainer;


/**
 * Initialisation parameters for {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodePO extends NamedPO {

	private PContainer<Codelink.Private> links = new PContainer<Private>(Collections.<Private>emptyList());	
	
	/**
	 * Creates an instance with an identifier.
	 * @param id the identifier
	 */
	public CodePO(String id) {
		super(id);
	}
	
	public PContainer<Codelink.Private> links() {
		return links;
	}
	
	public void setLinks(List<Codelink> links) {
		
		PContainer<Codelink.Private> privateLinks = new PContainer<Codelink.Private>(reveal(links,Codelink.Private.class));
		this.setLinks(privateLinks);
	}
	
	public void setLinks(PContainer<Codelink.Private> links) {
		notNull("links",links);
		
		propagateChangeFrom(links);
		
		this.links = links;
	}
}
