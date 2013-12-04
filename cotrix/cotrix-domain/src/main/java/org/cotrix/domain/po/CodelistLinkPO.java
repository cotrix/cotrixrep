package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.codelist.CodelistLink.Private;

/**
 * Initialisation parameters for {@link CodelistLink}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkPO extends NamedPO implements CodelistLink.State {

	private String targetId;
	
	public CodelistLinkPO(String id) {
		super(id);
	}

	
	public String targetId() {
		return targetId;
	}
	
	public void targetId(String id) {
		notNull("id",id);
		this.targetId=id;
	}
	
	@Override
	public Private entity() {
		return new CodelistLink.Private(this);
	}
	
}
