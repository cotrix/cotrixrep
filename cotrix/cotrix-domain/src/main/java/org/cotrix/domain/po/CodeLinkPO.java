package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.codelist.Codelink.Private;

/**
 * Initialisation parameters for {@link Codelink}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public final class CodeLinkPO extends AttributedPO implements Codelink.State {

	private String targetId;
	private CodelistLink.State definition;

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

	public void targetId(String id) {

		notNull("id",id);

		this.targetId = id;
	}

	public CodelistLink.State definition() {
		return definition;
	}

	public void definition(CodelistLink.State definition) {
		
		notNull("definition",definition);

		this.definition = definition;
	}
	
	public void definition(CodelistLink definition) {
		
		notNull("definition",definition);

		definition(reveal(definition,CodelistLink.Private.class));
	}
	
	@Override
	public Private entity() {
		return new Codelink.Private(this);
	}

}
