package org.cotrix.domain.simple.primitive;

import org.cotrix.domain.po.CodelistLinkPO;
import org.cotrix.domain.primitive.link.CodelistLink;
import org.cotrix.domain.utils.IdGenerator;

/**
 * Default implementation of {@link CodelistLink}.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleCodelistLink extends SimpleAttributedEntity<CodelistLink> implements CodelistLink  {

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

}
