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

	public static String getValue(UIQName name) {
		if (name == null) return "";
		return name.getLocalPart();
	}
}
