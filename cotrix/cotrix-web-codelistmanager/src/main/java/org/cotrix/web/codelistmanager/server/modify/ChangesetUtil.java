/**
 * 
 */
package org.cotrix.web.codelistmanager.server.modify;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.UICode;
import org.cotrix.web.codelistmanager.shared.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
public class ChangesetUtil {

	public static Attribute addAttribute(UIAttribute uiAttribute) {
		return attr().name(convert(uiAttribute.getName())).value(convert(uiAttribute.getValue()))
				.ofType(convert(uiAttribute.getType())).in(convert(uiAttribute.getLanguage())).build();
	}

	public static Attribute updateAttribute(UIAttribute uiAttribute) {
		return attr(uiAttribute.getId()).modify().name(convert(uiAttribute.getName()))
				.value(convert(uiAttribute.getValue())).ofType(convert(uiAttribute.getType()))
				.in(convert(uiAttribute.getLanguage())).build();
	}

	public static Attribute removeAttribute(String id) {
		return attr(id).delete();
	}

	public static List<Attribute> addAttributes(List<UIAttribute> uiAttributes) {
		if (uiAttributes.size() == 0)
			return Collections.emptyList();
		List<Attribute> attributes = new ArrayList<Attribute>(uiAttributes.size());
		for (UIAttribute uiAttribute : uiAttributes)
			attributes.add(addAttribute(uiAttribute));
		return attributes;
	}

	public static Code addCode(UICode uicode) {
		// FIXME tmp workaround
		if (uicode.getAttributes() == null || uicode.getAttributes().isEmpty())
			return code().name(convert(uicode.getName())).build();
		else
			return code().name(convert(uicode.getName())).attributes(addAttributes(uicode.getAttributes())).build();
	}

	public static Code updateCode(String id, String name) {
		return code(id).modify().name(convert(name)).build();
	}

	public static Code removeCode(String id) {
		return code(id).delete();
	}

	public static Codelist updateCodelist(String id, UIQName name) {
		return codelist(id).modify().name(convert(name)).build();
	}

	protected static String convert(String value) {
		return (value == null || value.isEmpty()) ? null : value;
	}

	protected static QName convert(UIQName value) {
		return (value == null || value.getLocalPart() == null || value.getLocalPart().isEmpty()) ? null : new QName(
				value.getNamespace(), value.getLocalPart());
	}
}
