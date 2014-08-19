package org.cotrix.domain.common;

import static java.text.DateFormat.*;
import static org.cotrix.common.CommonUtils.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.trait.Named;

public class Containers {

	//common to entity and bean containers
	public interface BaseContainer<T> extends Iterable<T> {
		
		int size();
		
		
		//id-based access
		
		boolean contains(String id);
		
		T lookup(String id); //may be null
		
		
		//name-based access
		
		boolean contains(QName name);
		
		T getFirst(QName name); //null or first
		
		Collection<T> get(QName name);  
		
		
	}
	
	
	
	public static class Codes extends Container.Private<Code.Private,Code.Bean> {
		
		public Codes(Codelist.Bean list) {
			super(list.codes());
		}
	}
	
	public static class Links extends Container.Private<Link.Private,Link.Bean> {
		
		public Links(Container.Bean<Link.Bean> beans) {
			super(beans);
		}
	}
	
	public static class AttributeDefinitions extends Container.Private<AttributeDefinition.Private,AttributeDefinition.Bean> {
		
		public AttributeDefinitions(Codelist.Bean list) {
			super(list.attributeDefinitions());
		}
	}
	
	public static class LinkDefinitions extends Container.Private<LinkDefinition.Private,LinkDefinition.Bean> {
		
		public LinkDefinitions(Codelist.Bean list) {
			super(list.linkDefinitions());
		}
	}

	
	public static class Attributes extends Container.Private<Attribute.Private,Attribute.Bean> {

		public Attributes() {
			super();
		}
		
		public Attributes(Container.Bean<Attribute.Bean> beans) {
			super(beans);
		}
		
		public Attributes(Attribute.Private ... attrs) {
			super(attrs);
		}
		
		public Date dateOf(Named named) {
			
			String val = valueOf(named);
			
			try {
				return val==null? null : getDateTimeInstance().parse(val);
			}
			catch(ParseException e) {
				throw unchecked(e);
			}
		}
		
		public QName nameOf(Named named) {
			
			String val = valueOf(named);
			
			try {
				return val==null? null : QName.valueOf(val);
			}
			catch(Exception e) {
				throw unchecked(e);
			}
		}
		
		
		public String valueOf(Named def) {
			
			Attribute a  = getFirst(def);
			
			return a==null ? null : a.value();
		
		}
	}

}
