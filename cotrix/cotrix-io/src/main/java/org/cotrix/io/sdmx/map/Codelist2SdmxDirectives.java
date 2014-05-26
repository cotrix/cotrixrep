package org.cotrix.io.sdmx.map;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.io.sdmx.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.io.MapService.MapDirectives;
import org.cotrix.io.sdmx.SdmxElement;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class Codelist2SdmxDirectives implements MapDirectives<CodelistBean> {

	//API support clauses
	
	public static interface GetClause {
		
		SdmxElement get(Attribute a);
		
	}
	
	public static interface TargetClause {
		
		Codelist2SdmxDirectives forCodelist();
		
		Codelist2SdmxDirectives forCodes();
	}

	public static interface MappingClause {
		
		TargetClause to(SdmxElement e);
	}
	
	
	
	
	
	public static Codelist2SdmxDirectives DEFAULT = new Codelist2SdmxDirectives();

	private final Map<AttributeTemplate,SdmxElement> attributeDirectives = new HashMap<AttributeTemplate,SdmxElement>();
	private final Map<AttributeTemplate,SdmxElement> codelistAttributeDirectives = new HashMap<AttributeTemplate,SdmxElement>();
	
	private String agency = DEFAULT_SDMX_AGENCY;
	private String name;
	private String version;
	private Boolean isFinal;
	
	
	public void name(String name) {
		this.name = name;
	}
	
	public Boolean isFinal() {
		return isFinal;
	}
	
	public void isFinal(Boolean isfinal) {
		this.isFinal = isfinal;
	}
	
	public String name() {
		return name;
	}
	
	public String agency() {
		return agency;
	}
	
	public void agency(String agency) {
		this.agency = agency;
	}
	
	public void version(String version) {
		this.version = version;
	}
	
	public String version() {
		return version;
	}
	
	public GetClause forCodelist() {
		
		return new GetClause() {
			
			public SdmxElement get(Attribute a) {
				
				return codelistAttributeDirectives.get(template(a));
				
			};
		};
		
	}
	
	public GetClause forCodes() {
		
		return new GetClause() {
			
			public SdmxElement get(Attribute a) {
				
				return attributeDirectives.get(template(a));				
			};
		};
		
	}
	
	/**
	 * Maps an attribute name and type onto a SDMX element type
	 * @param name the name
	 * @param type the type
	 * @return the mapping clause
	 */
	public MappingClause map(final QName name, final QName type) {
		
		notNull("name",name);
		notNull("type",type);
		
		return new MappingClause() {
			
			@Override
			public TargetClause to(final SdmxElement element) {

				notNull("SDMX element",element);

				return new TargetClause() {
					
					@Override
					public Codelist2SdmxDirectives forCodes() {

						attributeDirectives.put(template(name, type),element);
						
						return Codelist2SdmxDirectives.this;
					}
					
					@Override
					public Codelist2SdmxDirectives forCodelist() {
						
						codelistAttributeDirectives.put(template(name, type),element);
						
						return Codelist2SdmxDirectives.this;
					}
				};
				
				
			}
		};
		
		
	}
	
	/**
	 * Maps an attribute name and type onto a SDMX element type
	 * @param name the name
	 * @param type the type
	 * @return the mapping clause
	 */
	public MappingClause map(String name, String type) {
		
		notNull("name",name);
		notNull("type",type);
		
		return map(q(name),q(type));
		
	}

	
	//helpers
	
	static AttributeTemplate template(Attribute a) {
		return template(a.name(),a.type());
	}
	
	
	static AttributeTemplate template(QName name, QName type) {
		AttributeTemplate template = new AttributeTemplate();
		template.name=name;
		template.type=type;
		return template;
	}
	
	static class AttributeTemplate {
		
		QName name, type;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
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
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}
		
		
	}
}
