/**
 * 
 */
package org.cotrix.web.codelistmanager.client.util;

import org.cotrix.web.share.shared.codelist.UIAttribute;
import org.cotrix.web.share.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Attributes {
	
	public static boolean isSystemAttribute(UIAttribute attribute) {
		if (attribute == null) return false;
		return isSystemType(attribute.getType());
	}
	
	protected static boolean isSystemType(UIQName type)
	{
		if (type == null) return false;
		return Constants.SYSTEM_TYPE.equals(type);
	}

}
