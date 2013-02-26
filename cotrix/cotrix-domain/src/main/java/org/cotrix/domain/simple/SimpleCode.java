package org.cotrix.domain.simple;

import org.cotrix.domain.Code;
import org.cotrix.domain.pos.CodePO;
import org.cotrix.domain.utils.IdGenerator;


/**
 * Default {@link Code} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleCode extends SimpleAttributedObject<Code> implements Code {

	/**
	 * Creates an instance with a given name and attributes.
	 * @param name the name
	 * @param attributes the attributes
	 */
	public SimpleCode(CodePO params) {
		super(params);
	}
	
	protected void buildPO(CodePO po) {
		super.fillPO(po);
	}
	
	@Override
	public SimpleCode copy(IdGenerator generator) {
		CodePO po = new CodePO(generator.generateId());
		super.fillPO(generator,po);
		return new SimpleCode(po);
	}
	
	@Override
	public void update(Code delta) throws IllegalArgumentException, IllegalStateException {
		super.update(delta); //just for clarity and as a reminder for evolution
	}

	@Override
	public String toString() {
		return "Code [id="+id()+", name=" + name() + ", attributes=" + attributes()+"]" ;
	}

	

	
}
