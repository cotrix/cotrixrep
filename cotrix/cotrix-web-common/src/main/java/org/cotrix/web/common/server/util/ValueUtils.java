/**
 * 
 */
package org.cotrix.web.common.server.util;

import javax.xml.namespace.QName;

import org.cotrix.web.common.shared.codelist.UIQName;

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
	
	public static QName toQName(UIQName name) {
		return new QName(name.getNamespace(), name.getLocalPart());
	}
	
	public static String getLocalPart(UIQName name) {
		if (name == null) return null;
		return name.getLocalPart();
	}

}
