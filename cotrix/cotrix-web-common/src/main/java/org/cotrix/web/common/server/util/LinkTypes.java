/**
 * 
 */
package org.cotrix.web.common.server.util;

import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.links.ValueType;
import org.cotrix.domain.utils.AttributeTemplate;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.link.AttributeType;
import org.cotrix.web.common.shared.codelist.link.CodeNameType;
import org.cotrix.web.common.shared.codelist.link.UILinkType;
import org.cotrix.web.common.shared.codelist.link.UILinkType.UIValueType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypes {
	
	private static final CodeNameType CODE_NAME_TYPE = new CodeNameType(); 
	
	public static UILinkType toLinkType(CodelistLink codelistLink) {
		UIQName name = ValueUtils.safeValue(codelistLink.name());
		UICodelist targetCodelist = Codelists.toUICodelist(codelistLink.target());
		UIValueType valueType = toValueType(codelistLink.valueType());
		
		return new UILinkType(codelistLink.id(), name, targetCodelist, null, valueType);
	}
	
	private static UIValueType toValueType(ValueType valueType) {
		if (valueType instanceof NameLink) {
			return CODE_NAME_TYPE;
		}
		if (valueType instanceof AttributeLink) {
			AttributeLink attributeLink = (AttributeLink)valueType;
			AttributeTemplate template = attributeLink.template();
			UIQName name = ValueUtils.safeValue(template.name());
			UIQName type = ValueUtils.safeValue(template.type());
			String language = template.language();
			return new AttributeType(name, type, language);
		}
		
		throw new IllegalArgumentException("Unknown value type :"+valueType);
	}

}
