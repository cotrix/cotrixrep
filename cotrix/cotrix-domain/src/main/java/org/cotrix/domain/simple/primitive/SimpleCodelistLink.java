package org.cotrix.domain.simple.primitive;

import org.cotrix.domain.po.CodelistLinkPO;
import org.cotrix.domain.primitive.link.CodelistLink;
import org.cotrix.domain.spi.IdGenerator;

/**
 * Default implementation of {@link CodelistLink}.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleCodelistLink extends SimpleNamedEntity<CodelistLink> implements CodelistLink  {

	private String targetId;

	/**
	 * Creates an instance with given parameters.
	 * @param params the parameters
	 */
	public SimpleCodelistLink(CodelistLinkPO params) {
		super(params);
		this.targetId = params.targetId();	
	}

	@Override
	public String targetId() {
		return targetId;
	}
	
	@Override
	public void update(CodelistLink delta) throws IllegalArgumentException, IllegalStateException {
		super.update(delta);
		
		if (!targetId.equals(delta.targetId()))
			targetId=delta.targetId();
	}
	
	//fills PO for copy/versioning purposes
	private final void fillPO(IdGenerator generator,CodelistLinkPO po) {
		super.fillPO(generator,po);
		po.setTargetId(targetId);
	}
	
	@Override
	public CodelistLink copy(IdGenerator generator) {
		CodelistLinkPO po = new CodelistLinkPO(generator.generateId());
		this.fillPO(generator,po);
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((targetId == null) ? 0 : targetId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleCodelistLink other = (SimpleCodelistLink) obj;
		if (targetId == null) {
			if (other.targetId != null)
				return false;
		} else if (!targetId.equals(other.targetId))
			return false;
		return true;
	}

	
}
