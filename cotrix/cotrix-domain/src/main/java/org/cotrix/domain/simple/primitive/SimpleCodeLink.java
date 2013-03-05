package org.cotrix.domain.simple.primitive;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.po.CodeLinkPO;
import org.cotrix.domain.primitive.container.Bag;
import org.cotrix.domain.primitive.link.CodeLink;
import org.cotrix.domain.primitive.link.CodelistLink;
import org.cotrix.domain.utils.IdGenerator;

/**
 * Default {@link CodeLink} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleCodeLink extends SimpleEntity<CodeLink> implements CodeLink  {

	private final CodelistLink definition;
	private final Bag<Attribute> attributes;
	private String targetId;
	
	/**
	 * Creates a new instance with given parameters
	 * @param params the parameters
	 */
	public SimpleCodeLink(CodeLinkPO params) {
		
		super(params);
		
		this.targetId = params.targetId();
		this.attributes=params.attributes();
		this.definition=params.definition();
	}

	
	@Override
	public String targetId() {
		return targetId;
	}
	
	@Override
	public Bag<Attribute> attributes() {
		return attributes;
	}
	
	@Override
	public CodelistLink definition() {
		return definition;
	}
	
	@Override
	public void update(CodeLink delta) throws IllegalArgumentException ,IllegalStateException {
		
		super.update(delta);

		this.attributes.update(delta.attributes());
		this.definition.update(delta.definition());
		
		if (!targetId.equals(delta.targetId()))
			targetId=delta.targetId();
	}
	
	//fills PO for copy/versioning purposes
	protected void fillPO(IdGenerator generator,CodeLinkPO po) {
		
		po.setAttributes(attributes.copy(generator));
		po.setDefinition(definition.copy(generator));
		po.setTargetId(targetId);
		
	}
	
	@Override
	public SimpleCodeLink copy(IdGenerator generator) {
		CodeLinkPO po = new CodeLinkPO(generator.generateId());
		fillPO(generator,po);
		return new SimpleCodeLink(po);
	}



}
