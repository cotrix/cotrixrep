package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import org.cotrix.domain.primitive.link.CodelistLink;

/**
 * Initialisation parameters for {@link CodelistLink}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkPO extends NamedPO {

	private String targetId;
	
	public CodelistLinkPO(String id) {
		super(id);
	}

	
	public String targetId() {
		return targetId;
	}
	
	public void setTargetId(String id) {
		notNull(id);
		this.targetId=id;
	}
}
