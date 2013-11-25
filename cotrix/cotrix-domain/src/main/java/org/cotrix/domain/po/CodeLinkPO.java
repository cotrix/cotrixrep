package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.CodelistLink;

/**
 * Initialisation parameters for {@link Codelink}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public final class CodeLinkPO extends AttributedPO {

	private String targetId;
	private CodelistLink.Private definition;

	/**
	 * Creates an instance with an identifier.
	 * 
	 * @param id the identifier
	 */
	public CodeLinkPO(String id) {
		super(id);
	}

	public String targetId() {
		return targetId;
	}

	public void setTargetId(String id) {

		notNull("id",id);

		this.targetId = id;
	}

	public CodelistLink.Private definition() {
		return definition;
	}

	public void setDefinition(CodelistLink.Private definition) {
		
		notNull("definition",definition);

		this.definition = definition;
	}
	
	public void setDefinition(CodelistLink definition) {
		
		notNull("definition",definition);

		setDefinition(reveal(definition,CodelistLink.Private.class));
	}

}
