package org.cotrix.io.sdmx.map;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService.MapDirectives;
import org.cotrix.io.sdmx.SdmxElement;


/**
 * {@link ImportDirectives} for codelists available in SDMX.
 * 
 * @author Fabio Simeoni
 *
 */
public class Sdmx2CodelistDirectives implements MapDirectives<Codelist> {

	public static final Sdmx2CodelistDirectives DEFAULT  = new Sdmx2CodelistDirectives();
	
	private String version;
	
	private QName name;
	
	private Map<SdmxElement,QName> names = new HashMap<SdmxElement,QName>();
	
	/**
	 * Creates an instance with default directives.
	 */
	public Sdmx2CodelistDirectives() {
		
		for (SdmxElement e : SdmxElement.values())
			map(e,e.defaultName());
	}
	
	public void map(SdmxElement element, QName name) {
		this.names.put(element,name);
	}
	
	public void ignore(SdmxElement element) {
		this.names.remove(element);
	}
	
	public boolean isIncluded(SdmxElement element) {
		return names.containsKey(element);
	}
	
	public QName get(SdmxElement element) {
		return names.get(element);
	}
	
	
	/**
	 * Sets the name of the target codelist for these directives.
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public Sdmx2CodelistDirectives name(QName name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Sets the name of the target codelist for these directives.
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public Sdmx2CodelistDirectives name(String name) {
		return name(new QName(name));
	}
	
	/**
	 * Returns the name of the target codelist for these directives.
	 * 
	 * @return the name
	 */
	public QName name() {
		return name;
	}
	
	/**
	 * Returns the version of the target codelist for these directives.
	 * @return the version
	 */
	public String version() {
		return version;
	}
	
	/**
	 * Sets the version of the target codelist for these directives
	 * @param version
	 * @return these directives
	 */
	public Sdmx2CodelistDirectives version(String version) {
		this.version = version;
		return this;
	}
	
}
