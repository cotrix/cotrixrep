/**
 * 
 */
package org.cotrix.web.codelistmanager.client.util;

import org.cotrix.web.share.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Attributes {
	
	public static boolean isSystemAttribute(UIQName type)
	{
		return Constants.SYSTEM_TYPE.equals(type);
	}

}
