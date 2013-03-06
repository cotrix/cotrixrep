package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Code;
import org.cotrix.domain.primitive.container.MutableContainer;
import org.cotrix.domain.primitive.link.CodeLink;


/**
 * Initialisation parameters for {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodePO extends NamedPO {

	private MutableContainer<CodeLink> links = new MutableContainer<CodeLink>(Collections.<CodeLink>emptyList());	
	
	/**
	 * Creates an instance with an identifier.
	 * @param id the identifier
	 */
	public CodePO(String id) {
		super(id);
	}
	
	public MutableContainer<CodeLink> links() {
		return links;
	}
	
	public void setLinks(MutableContainer<CodeLink> links) {
		notNull("links",links);
		
		propagateChangeFrom(links);
		
		this.links = links;
	}
}
