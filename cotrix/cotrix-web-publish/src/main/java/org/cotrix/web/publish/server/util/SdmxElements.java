/**
 * 
 */
package org.cotrix.web.publish.server.util;

import org.cotrix.io.sdmx.SdmxElement;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SdmxElements {
	
	public static SdmxElement toSdmxElement(org.cotrix.web.publish.shared.SdmxElement element) {
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

}
