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
public final class CodeLinkPO extends AttributedPO<Codelink.Private> implements Codelink.State {

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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + ((targetId == null) ? 0 : targetId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof CodeLinkPO))
			return false;
		CodeLinkPO other = (CodeLinkPO) obj;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (targetId == null) {
			if (other.targetId != null)
				return false;
		} else if (!targetId.equals(other.targetId))
			return false;
		return true;
	}
	
	

}
