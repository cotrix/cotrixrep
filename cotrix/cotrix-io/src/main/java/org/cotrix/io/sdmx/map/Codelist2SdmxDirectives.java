package org.cotrix.io.sdmx.map;

import static org.cotrix.io.sdmx.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.io.MapService.MapDirectives;
import org.cotrix.io.sdmx.SdmxElement;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class Codelist2SdmxDirectives implements MapDirectives<CodelistBean> {

	public static final Codelist2SdmxDirectives DEFAULT = new Codelist2SdmxDirectives();

	
	private Map<QName,SdmxElement> elements = new HashMap<QName,SdmxElement>();
	
	private String agency = DEFAULT_SDMX_AGENCY;
	private String name;
	private String version;
	private Boolean isFinal;
	
	/**
	 * Creates an instance with default directives.
	 */
	public Codelist2SdmxDirectives() {

		for (SdmxElement e : SdmxElement.values())
			map(e.defaultType(),e);
	}
	
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
	
	public void map(QName name,SdmxElement element) {
		
		elements.put(name,element);
	}
	
	public SdmxElement get(QName name) {
		return elements.get(name);
	}
}
