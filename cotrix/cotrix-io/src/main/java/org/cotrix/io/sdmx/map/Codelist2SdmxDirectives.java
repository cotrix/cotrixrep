package org.cotrix.io.sdmx.map;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.io.sdmx.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.io.MapService.MapDirectives;
import org.cotrix.io.sdmx.SdmxElement;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class Codelist2SdmxDirectives implements MapDirectives<CodelistBean> {

	public static interface MappingClause {
		
		/**
		 * Maps the attribute name and type onto an SDMX element type
		 * @param to the element type
		 * @return this directives
		 */
		Codelist2SdmxDirectives to(SdmxElement e);
	}
	
	public static Codelist2SdmxDirectives DEFAULT = new Codelist2SdmxDirectives();

	private final Map<Attribute,SdmxElement> attributeDirectives = new HashMap<Attribute,SdmxElement>();
	
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
	
	
	public SdmxElement get(Attribute attribute) {
		
		return attributeDirectives.get(canonicalFormOf(attribute));
		
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
			public Codelist2SdmxDirectives to(SdmxElement element) {
				notNull("SDMX element",element);
				
				attributeDirectives.put(templateFrom(name, type),element);
				
				return Codelist2SdmxDirectives.this;
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
	
	public Attribute canonicalFormOf(Attribute a) {
		return templateFrom(a.name(),a.type());
	}
	
	public Attribute templateFrom(QName name, QName type) {
		return modifyAttribute("*").name(name).value("?").ofType(type).build();
	}
	
}
