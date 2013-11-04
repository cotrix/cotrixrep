package org.cotrix.io.sdmx;

import static org.cotrix.io.sdmx.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.io.publish.PublicationDirectives;

public class SdmxPublishDirectives implements PublicationDirectives {

	public static final SdmxPublishDirectives DEFAULT = new SdmxPublishDirectives() {
	};

	
	private Map<QName,SdmxElement> elements = new HashMap<QName,SdmxElement>();
	
	private String agency = default_sdmx_agency;
	private String name;
	private String version;
	private Boolean isFinal;
	
	/**
	 * Creates an instance with default directives.
	 */
	public SdmxPublishDirectives() {

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
