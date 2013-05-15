package org.cotrix.io.sdmx;

import static org.cotrix.io.sdmx.SdmxImportDirectives.SdmxElement.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.io.ingest.ImportDirectives;


/**
 * {@link ImportDirectives} for codelists available in SDMX.
 * 
 * @author Fabio Simeoni
 *
 */
public class SdmxImportDirectives implements ImportDirectives {

	public static final SdmxImportDirectives DEFAULT  = new SdmxImportDirectives();
	
	private static String sdmx = "http://www.sdmx.org";
	
	public static enum SdmxElement {FINAL,AGENCY, VALID_FROM, VALID_TO, NAME, DESCRIPTION, ANNOTATION}
	
	public SdmxImportDirectives() {
		
		map(FINAL,new QName(sdmx,"final"));
		map(AGENCY,new QName(sdmx,"agency"));
		map(VALID_FROM,new QName(sdmx,"validFrom"));
		map(VALID_TO,new QName(sdmx,"validTo"));
		map(NAME,new QName(sdmx,"name "));
		map(DESCRIPTION,new QName(sdmx,"description"));
		map(ANNOTATION,new QName(sdmx,"annotation"));
	}
	
	private Map<SdmxElement,QName> names = new HashMap<SdmxElement,QName>();
	
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
	
	
	
}
