/**
 * 
 */
package org.cotrix.web.publish.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefinitionMapping implements IsSerializable {
	
	public interface MappingTarget extends IsSerializable {
		public String getLabel();
	}
	
	protected UIDefinition definition;
	protected MappingTarget target;
	protected boolean mapped;
	
	public UIDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(UIDefinition definition) {
		this.definition = definition;
	}

	public boolean isMapped() {
		return mapped;
	}
	
	public void setMapped(boolean mapped) {
		this.mapped = mapped;
	}

	public MappingTarget getTarget() {
		return target;
	}

	public void setTarget(MappingTarget target) {
		this.target = target;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DefinitionMapping [definition=");
		builder.append(definition);
		builder.append(", target=");
		builder.append(target);
		builder.append(", mapped=");
		builder.append(mapped);
		builder.append("]");
		return builder.toString();
	}
}
