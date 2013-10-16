/**
 * 
 */
package org.cotrix.web.codelistmanager.server.modify;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ChangesetUtil {
	
	public static Attribute addAttribute(UIAttribute uiAttribute)
	{
		return attr().add().name(uiAttribute.getName()).value(uiAttribute.getValue()).ofType(uiAttribute.getType()).in(uiAttribute.getLanguage()).build();
	}
	
	public static Attribute updateAttribute(UIAttribute uiAttribute)
	{
		return attr(uiAttribute.getId()).modify().name(uiAttribute.getName()).value(uiAttribute.getValue()).ofType(uiAttribute.getType()).in(uiAttribute.getLanguage()).build();
	}
	
	public static Attribute removeAttribute(String id)
	{
		return attr(id).delete();
	}
	
	public static List<Attribute> addAttributes(List<UIAttribute> uiAttributes)
	{
		if (uiAttributes.size() == 0) return Collections.emptyList();
		List<Attribute> attributes = new ArrayList<Attribute>(uiAttributes.size());
		for (UIAttribute uiAttribute:uiAttributes) attributes.add(addAttribute(uiAttribute));
		return attributes;
	}
	
	public static Code addCode(UICode uicode)
	{
		return code().add().name(uicode.getName()).attributes(addAttributes(uicode.getAttributes())).build();
	}
	
	public static Code updateCode(String id, String name)
	{
		return code(id).modify().name(name).build();
	}
	
	public static Code removeCode(String id)
	{
		return code(id).delete();
	}
}
