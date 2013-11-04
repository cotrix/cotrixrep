package org.cotrix.io.sdmx;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.io.map.MapDirectives;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;


/**
 * {@link ImportDirectives} for codelists available in SDMX.
 * 
 * @author Fabio Simeoni
 *
 */
public class SdmxMapDirectives implements MapDirectives<CodelistBean> {

	public static final SdmxMapDirectives DEFAULT  = new SdmxMapDirectives();
	
	private String version;
	
	private QName name;
	
	private Map<SdmxElement,QName> names = new HashMap<SdmxElement,QName>();
	
	/**
	 * Creates an instance with default directives.
	 */
	public SdmxMapDirectives() {
		
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
	public SdmxMapDirectives name(QName name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Sets the name of the target codelist for these directives.
	 * 
	 * @param name the name
	 * @return these directives
	 */
	public SdmxMapDirectives name(String name) {
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
	public SdmxMapDirectives version(String version) {
		this.version = version;
		return this;
	}
}
