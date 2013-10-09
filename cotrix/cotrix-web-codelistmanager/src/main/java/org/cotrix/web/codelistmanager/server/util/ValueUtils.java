/**
 * 
 */
package org.cotrix.web.codelistmanager.server.util;

import javax.xml.namespace.QName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ValueUtils {
	
	public static String safeValue(String value)
	{
		return value==null?"":value;
	}
	
	public static String safeValue(QName value)
	{
		return value==null?"":value.toString();
	}

}
