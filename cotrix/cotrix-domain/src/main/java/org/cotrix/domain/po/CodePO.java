package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Code;
import org.cotrix.domain.primitive.container.Bag;
import org.cotrix.domain.primitive.link.CodeLink;


/**
 * A set of parameters required to create a {@link Code}.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodePO extends AttributedPO {

	private Bag<CodeLink> links = new Bag<CodeLink>(Collections.<CodeLink>emptyList());	
	
	/**
	 * Creates an instance with an identifier.
	 * @param id the identifier
	 */
	public CodePO(String id) {
		super(id);
	}
	
	public Bag<CodeLink> links() {
		return links;
	}
	
	public void setLinks(Bag<CodeLink> links) {
		notNull("links",links);
		
		propagateChangeFrom(links);
		
		this.links = links;
	}
}
