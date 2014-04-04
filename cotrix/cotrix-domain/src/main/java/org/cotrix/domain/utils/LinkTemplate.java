package org.cotrix.domain.utils;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.links.ValueType;

public class LinkTemplate {

	private final QName name;
	private final String id;
	private final ValueType type;
	
	public LinkTemplate(CodelistLink template) {
		
		notNull("template",template);
		
		this.name = template.name();
		this.id = template.target() == null? null : template.target().id();
		
		ValueType type = template.valueType();
		this.type = type==NameLink.INSTANCE? null: type;
	}
	
	public QName linkName() {
		return name;
	}
	
	public String targetId() {
		return id;
	}
	
	public ValueType valueType() {
		return type;
	}
	
	public boolean matches(CodelistLink link) {
		
		return matches(reveal(link).state());
	}

	public boolean matches(CodelistLink.State link) {
		
		return matches(name, link.name()) 
			   && matches(id, link.target()) 
			   && matches(type==null?NameLink.INSTANCE:type, link.valueType());
	}
	
	private boolean matches(String id, Codelist.State val) {
		
		return id == null || (val!=null && id.equals(val.id()));
	}

	private boolean matches(Object template, Object val) {
		
		return (template == null || template.equals(NULL_QNAME) || template.equals(NULL_STRING) || template.equals(val));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		LinkTemplate other = (LinkTemplate) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	
	
}
