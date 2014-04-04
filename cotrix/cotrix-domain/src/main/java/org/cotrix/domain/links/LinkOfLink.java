package org.cotrix.domain.links;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.utils.LinkTemplate;

public class LinkOfLink implements ValueType {

	private final LinkTemplate template;
	
	public LinkOfLink(LinkTemplate template) {
		this.template=template;
	}
	
	public LinkTemplate template() {
		return template;
	}
	
	@Override
	public Object valueIn(Code.State code) {
		
		List<Object> matches = new ArrayList<>();
		
		for (Codelink.State link : code.links()) {
			
			CodelistLink.State linktype = link.type();
			
			if (template.matches(linktype)) 
				matches.add(Codelink.Private.resolve(link,linktype));
		}
		
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
		LinkOfLink other = (LinkOfLink) obj;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}
	
	
}
