package org.cotrix.domain.links;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.utils.AttributeTemplate;

public class AttributeLink implements LinkType {

	private final AttributeTemplate template;
	
	public AttributeLink(AttributeTemplate template) {
		this.template=template;
	}
	
	
	@Override
	public Object value(Code.State code) {
	
		List<Object> matches = new ArrayList<>();
		
		for (Attribute.State a : code.attributes())
			if (template.matches(a))
				matches.add(a.value());
		
		
		return matches;
 
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
		AttributeLink other = (AttributeLink) obj;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}
	
	
}
