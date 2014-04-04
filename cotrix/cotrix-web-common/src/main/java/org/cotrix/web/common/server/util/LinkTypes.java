/**
 * 
 */
package org.cotrix.web.common.server.util;

import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.ValueType;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.link.UILinkType;
import org.cotrix.web.common.shared.codelist.link.UILinkType.UIValueType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypes {
	
	public static UILinkType toLinkType(CodelistLink codelistLink) {
		UIQName name = ValueUtils.safeValue(codelistLink.name());
		UICodelist targetCodelist = Codelists.toUICodelist(codelistLink.target());
		//codelistLink.valueType()
		
		//return new UILinkType(codelistLink.id(), name, targetCodelist, valueFunction, valueType);
		return null;
	}
	
	private static UIValueType toValueType(ValueType type) {
		if (type instanceof AttributeLink) {
			AttributeLink attributeLink = (AttributeLink)type;
			
		}
		return null;
	}

}
