package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;


public class CodelistLinkPO extends AttributedPO {

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
