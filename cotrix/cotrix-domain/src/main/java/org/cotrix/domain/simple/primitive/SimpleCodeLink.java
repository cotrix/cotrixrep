package org.cotrix.domain.simple.primitive;

import org.cotrix.domain.Codelink;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.po.CodeLinkPO;
import org.cotrix.domain.spi.IdGenerator;

/**
 * Default {@link Codelink} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleCodeLink extends SimpleAttributed<Codelink.Private> implements Codelink.Private  {

	private final CodelistLink.Private definition;
	private String targetId;
	
	/**
	 * Creates a new instance with given parameters
	 * @param params the parameters
	 */
	public SimpleCodeLink(CodeLinkPO params) {
		
		super(params);
		
		this.targetId = params.targetId();
		this.definition=params.definition();
	}

	
	@Override
	public String targetId() {
		return targetId;
	}
	
	@Override
	public CodelistLink.Private definition() {
		return definition;
	}
	
	@Override
	public void update(Codelink.Private delta) throws IllegalArgumentException ,IllegalStateException {
		
		super.update(delta);

		this.definition.update(delta.definition());
		
		if (!targetId.equals(delta.targetId()))
			targetId=delta.targetId();
	}
	
	//fills PO for copy/versioning purposes
	protected void fillPO(IdGenerator generator,CodeLinkPO po) {
		
		super.fillPO(generator, po);
		po.setDefinition(definition.copy(generator));
		po.setTargetId(targetId);
		
	}
	
	public SimpleCodeLink copy(IdGenerator generator) {
		CodeLinkPO po = new CodeLinkPO(generator.generateId());
		fillPO(generator,po);
		return new SimpleCodeLink(po);
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
		if (getClass() != obj.getClass())
			return false;
		SimpleCodeLink other = (SimpleCodeLink) obj;
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
