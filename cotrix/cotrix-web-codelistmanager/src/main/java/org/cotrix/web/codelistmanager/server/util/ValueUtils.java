/**
 * 
 */
package org.cotrix.web.codelistmanager.server.util;

import javax.xml.namespace.QName;

import org.cotrix.web.share.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ValueUtils {
	
	public static String safeValue(String value)
	{
		return value==null?"":value;
	}
	
	public static UIQName safeValue(QName value)
	{
		return value==null?new UIQName("", ""):new UIQName(value.getNamespaceURI(), value.getLocalPart());
	}

}
