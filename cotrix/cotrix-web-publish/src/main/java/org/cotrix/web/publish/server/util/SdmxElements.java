/**
 * 
 */
package org.cotrix.web.publish.server.util;

import javax.xml.namespace.QName;

import org.cotrix.io.sdmx.SdmxElement;
import org.cotrix.web.publish.shared.UISdmxElement;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SdmxElements {
	
	public static SdmxElement toSdmxElement(UISdmxElement element) {
		switch (element) {
			case AGENCY: return SdmxElement.AGENCY;
			case ANNOTATION: return SdmxElement.ANNOTATION;
			case DESCRIPTION: return SdmxElement.DESCRIPTION;
			case FINAL: return SdmxElement.FINAL;
			case NAME: return SdmxElement.NAME;
			case URI: return SdmxElement.URI;
			case VALID_FROM: return SdmxElement.VALID_FROM;
			case VALID_TO: return SdmxElement.VALID_TO;
			default: throw new IllegalArgumentException("Unknown type SmdxElement "+element);
		}
	}
	
	public static SdmxElement findSdmxElement(QName name, QName type) {
		for (SdmxElement element:SdmxElement.values()) {
			if (element.defaultName().equals(name) && element.defaultType().equals(type)) return element;
		}
		return findUnqualifiedSdmxElement(name.getLocalPart(), type.getLocalPart());
	}
	
	public static SdmxElement findUnqualifiedSdmxElement(String name, String type) {
		System.out.println("findUnqualifiedSdmxElement name: "+name+" type: "+type);
		for (SdmxElement element:SdmxElement.values()) {
			System.out.println("checking "+element+" name: "+element.defaultName()+" type: "+element.defaultType());
			if (element.defaultName().getLocalPart().equals(name) && element.defaultType().getLocalPart().equals(type)) return element;
		}
		return  findSdmxElementByUnqualifiedType(type);
	}
	
	public static SdmxElement findSdmxElementByUnqualifiedType(String type) {
		System.out.println("findSdmxElementByType type: "+type);
		for (SdmxElement element:SdmxElement.values()) {
			System.out.println("checking "+element+" name: "+element.defaultName()+" type: "+element.defaultType());
			if (element.defaultType().getLocalPart().equals(type)) return element;
		}
		return SdmxElement.DESCRIPTION;
	}
	
	public static UISdmxElement toUISdmxElement(SdmxElement element) {
		switch (element) {
			case AGENCY: return UISdmxElement.AGENCY;
			case ANNOTATION: return UISdmxElement.ANNOTATION;
			case DESCRIPTION: return UISdmxElement.DESCRIPTION;
			case FINAL: return UISdmxElement.FINAL;
			case NAME: return UISdmxElement.NAME;
			case URI: return UISdmxElement.URI;
			case VALID_FROM: return UISdmxElement.VALID_FROM;
			case VALID_TO: return UISdmxElement.VALID_TO;
			default: throw new IllegalArgumentException("Unknown type SmdxElement "+element);
		}
	}

}
