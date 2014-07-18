package org.cotrix.domain.utils;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkOfLink;
import org.cotrix.domain.links.LinkValueType;
import org.cotrix.domain.links.NameLink;

public class LinkTemplate {

	private final QName name;
	private final QName target;
	private final QName anchor;

	public LinkTemplate(CodelistLink link) {
		
		notNull("link",link);
		
		this.name=link.qname();
		this.target=link.target().qname();
		this.anchor = anchorFor(link);
		
	}
	
	public LinkTemplate(QName name, QName target, QName anchor) {
		
		notNull("name",name);
		notNull("target",target);
		notNull("anchor",anchor);
		
		this.name=name;
		this.target=target;
		this.anchor=anchor;
	}
	
	public QName qname() {
		return name;
	}
	
	public QName target() {
		return target;
	}
	
	public QName anchor() {
		return anchor;
	}
	
	public boolean matches(CodelistLink link) {
	
		return similarity(link)==1;
	}
	
	public float similarity(CodelistLink link) {
		
		float score = 0;
		
		if (matches(name,link.qname()))
			score++;
		
		if (matches(target,link.target().qname()))
			score++;
		
		if (matches(anchor, anchorFor(link)))
			score++;
				
		return score/3;
	}

	//helpers
	
	private boolean matches(QName template, QName name) {
		return template!=null?template.equals(name):true;
	}

	private QName anchorFor(CodelistLink link) {
		
		LinkValueType type = link.valueType();
		
		if (type instanceof NameLink)
		
			return q(NS,"code");
		
		else 
			
			if (type instanceof AttributeLink) {
			
				return AttributeLink.class.cast(type).template().name();
			}
			
			else 
		
				if (type instanceof LinkOfLink) //protecting from future evolution
					
					return LinkOfLink.class.cast(type).target().qname();
				
				else 
					
					return null;
	}
	
	
}
