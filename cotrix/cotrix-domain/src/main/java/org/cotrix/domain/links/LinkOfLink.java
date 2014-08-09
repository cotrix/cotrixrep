package org.cotrix.domain.links;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Link;
import org.cotrix.domain.codelist.Codelist;

public class LinkOfLink implements LinkValueType {

	private LinkDefinition target;
	
	public LinkOfLink(LinkDefinition template) {
		
		notNull("template", template);
		
		if (reveal(template).isChangeset())
			throw new IllegalArgumentException("link template cannot be a changeset");
		
		this.target=template;
	}
	
	public LinkDefinition target() {
		return target;
	}
	
	@Override
	public List<Object> valueIn(String id, Code.Bean code, List<String> ids) {
		
		List<Object> matches = new ArrayList<>();
		
		for (Link.Bean link : code.links()) {
			
			LinkDefinition.Bean linktype = link.definition();
			
			if (matches(linktype))
				for (Object match : Link.Private.resolve(link,linktype,ids))
					matches.add(match);
		}
		
		return matches;
 
	}
	
	public boolean matches(LinkDefinition link) {
		
		return matches(reveal(link).bean());
	}

	public boolean matches(LinkDefinition.Bean link) {
		
		QName name = target.qname();
		String id = target.target() == null? null : target.target().id();
		LinkValueType type = target.valueType();

		
		return matches(name, link.qname()) 
			   && matches(id, link.target()) 
			   && matches(type==null?NameLink.INSTANCE:type, link.valueType());
	}
	
	private boolean matches(String id, Codelist.Bean val) {
		
		return id == null || (val!=null && id.equals(val.id()));
	}

	private boolean matches(Object templ, Object val) {
		
		return (templ == null || templ.equals(NULL_QNAME) || templ.equals(NULL_STRING) || templ.equals(val));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	
	
	
}
