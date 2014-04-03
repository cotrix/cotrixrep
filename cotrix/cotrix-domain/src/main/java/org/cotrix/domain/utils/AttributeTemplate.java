package org.cotrix.domain.utils;

import org.cotrix.domain.common.Attribute;

public class AttributeTemplate {

	private final Attribute template;
	
	public AttributeTemplate(Attribute template) {
		this.template=template;
	}
	
	public boolean matches(Attribute attribute) {
		
		return matches(template.name(), attribute.name()) &&
			   matches(template.type(), attribute.type()) &&
			   matches(template.language(), attribute.language());
	}
	
	private boolean matches(Object template, Object val) {
		
		return template == null || template.equals(val);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((template == null) ? 0 : template.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeTemplate other = (AttributeTemplate) obj;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}
	
	
}
