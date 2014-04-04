/**
 * 
 */
package org.cotrix.web.common.client.util;

import org.cotrix.web.common.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ValueUtils {
	
	public static final String defaultNamespace = "";
	
	public static UIQName getValue(String name) {
		if (name == null) return null;
		return new UIQName(defaultNamespace, name);
	}

	public static String getValue(UIQName name) {
		if (name == null) return "";
		return name.getLocalPart();
	}
	
	public static String getLocalPart(UIQName name) {
		if (name == null) return "";
		return name.getLocalPart();
	}
}
