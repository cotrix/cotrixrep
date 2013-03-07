package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.primitive.container.Bag;
import org.cotrix.domain.primitive.link.CodelistLink;

/**
 * A set of parameters required to create an {@link Attribute}.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodeLinkPO  extends EntityPO {

	private Bag<Attribute> attributes = new Bag<Attribute>(Collections.<Attribute>emptyList());
	private String targetId;
	private CodelistLink definition;
	
	/**
	 * Creates an instance with an identifier.
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
		
		this.targetId=id;
	}
	
	/**
	 * Returns the attributes parameter.
	 * @return the parameter
	 */
	public Bag<Attribute> attributes() {
		return attributes;
	}

	/**
	 * Sets the attribute parameter.
	 * @param attributes the parameter
	 */
	public void setAttributes(Bag<Attribute> attributes) {
		
		notNull(attributes);
		
		propagateChangeFrom(attributes);
		
		this.attributes = attributes;
	}
	
	public CodelistLink definition() {
		return definition;
	}
	
	public void setDefinition(CodelistLink definition) {
		notNull(definition);
		
		propagateChangeFrom(definition);
		
		this.definition = definition;
	}

}
