package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import org.cotrix.domain.Codelink;
import org.cotrix.domain.CodelistLink;

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

		notNull(id);

		this.targetId = id;
	}

	public CodelistLink.Private definition() {
		return definition;
	}

	public void setDefinition(CodelistLink.Private definition) {
		
		notNull(definition);

		propagateChangeFrom(definition);

		this.definition = definition;
	}
	
	public void setDefinition(CodelistLink definition) {
		
		notNull(definition);

		setDefinition(reveal(definition,CodelistLink.Private.class));
	}

}
