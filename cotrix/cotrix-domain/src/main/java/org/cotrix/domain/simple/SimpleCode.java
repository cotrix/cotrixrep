package org.cotrix.domain.simple;

import org.cotrix.domain.Code;
import org.cotrix.domain.po.CodePO;
import org.cotrix.domain.primitive.container.Container;
import org.cotrix.domain.primitive.container.MutableContainer;
import org.cotrix.domain.primitive.link.CodeLink;
import org.cotrix.domain.simple.primitive.SimpleNamedEntity;
import org.cotrix.domain.spi.IdGenerator;


/**
 * Default {@link Code} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleCode extends SimpleNamedEntity<Code> implements Code {

	private final MutableContainer<CodeLink> links;
	
	/**
	 * Creates an instance with given parameters.
	 * @param params the parameters
	 */
	public SimpleCode(CodePO params) {
		super(params);
		this.links=params.links();
	}
	
	@Override
	public Container<CodeLink> links() {
		return links;
	}
	
	//fills PO for copy/versioning purposes
	protected void fillPO(IdGenerator generator,CodePO po) {
		super.fillPO(generator,po);
		po.setLinks(links.copy(generator));
	}
	
	@Override
	public SimpleCode copy(IdGenerator generator) {
		CodePO po = new CodePO(generator.generateId());
		fillPO(generator,po);
		return new SimpleCode(po);
	}
	
	@Override
	public void update(Code delta) throws IllegalArgumentException, IllegalStateException {
		super.update(delta);
		this.links.update(delta.links());
	}

	@Override
	public String toString() {
		return "Code [id="+id()+", name=" + name() + ", attributes=" + attributes()+"]" ;
	}

	

	
}
