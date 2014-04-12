package org.cotrix.domain.links;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;

public class LinkOfLink implements ValueType {

	private CodelistLink target;
	
	public LinkOfLink(CodelistLink template) {
		
		notNull("template", template);
		
		if (reveal(template).isChangeset())
			throw new IllegalArgumentException("link template cannot be a changeset");
		
		this.target=template;
	}
	
	public CodelistLink target() {
		return target;
	}
	
	@Override
	public List<Object> valueIn(String id, Code.State code, List<String> ids) {
		
		List<Object> matches = new ArrayList<>();
		
		for (Codelink.State link : code.links()) {
			
			CodelistLink.State linktype = link.type();
			
			if (matches(linktype))
				for (Object match : Codelink.Private.resolve(link,linktype,ids))
					matches.add(match);
		}
		
		return matches;
 
	}
	
	public boolean matches(CodelistLink link) {
		
		return matches(reveal(link).state());
	}

	public boolean matches(CodelistLink.State link) {
		
		QName name = target.name();
		String id = target.target() == null? null : target.target().id();
		ValueType type = target.valueType();

		
		return matches(name, link.name()) 
			   && matches(id, link.target()) 
			   && matches(type==null?NameLink.INSTANCE:type, link.valueType());
	}
	
	private boolean matches(String id, Codelist.State val) {
		
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
