/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkOfLink;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.links.ValueFunction;
import org.cotrix.domain.links.ValueFunctions.CustomFunction;
import org.cotrix.domain.links.ValueFunctions.PrefixFunction;
import org.cotrix.domain.links.ValueFunctions.SuffixFunction;
import org.cotrix.domain.links.LinkValueType;
import org.cotrix.domain.utils.AttributeTemplate;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.linktype.AttributeType;
import org.cotrix.web.common.shared.codelist.linktype.CodeNameType;
import org.cotrix.web.common.shared.codelist.linktype.LinkType;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.common.shared.codelist.linktype.UIValueFunction;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType.UIValueType;
import org.cotrix.web.common.shared.codelist.linktype.UIValueFunction.Function;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypes {
	
	private static final CodeNameType CODE_NAME_TYPE = new CodeNameType();
	private static final ArrayList<String> EMPTY_ARGUMENTS = new ArrayList<>();
	
	public static UILinkType toUILinkType(CodelistLink codelistLink) {
		UIQName name = ValueUtils.safeValue(codelistLink.name());
		UICodelist targetCodelist = Codelists.toUICodelist(codelistLink.target());
		UIValueType valueType = toValueType(codelistLink.valueType());
		UIValueFunction valueFunction = toValueFunction(codelistLink.function());
		List<UIAttribute> attributes = Codelists.toUIAttributes(codelistLink.attributes());
		
		return new UILinkType(codelistLink.id(), name, targetCodelist, valueFunction, valueType, attributes);
	}
	
	private static UIValueType toValueType(LinkValueType valueType) {
		
		if (valueType instanceof NameLink) {
			return CODE_NAME_TYPE;
		}
		
		if (valueType instanceof AttributeLink) {
			AttributeLink attributeLink = (AttributeLink)valueType;
			AttributeTemplate template = attributeLink.template();
			UIQName name = ValueUtils.safeValue(template.name());
			UIQName type = ValueUtils.safeValue(template.type());
			String language = ValueUtils.safeValue(template.language());
			return new AttributeType(name, type, language);
		}

		if (valueType instanceof LinkOfLink) {
			LinkOfLink linkOfLink = (LinkOfLink)valueType;
			CodelistLink link = linkOfLink.target();
			String id = link.id();
			UIQName name = ValueUtils.safeValue(link.name());
			return new LinkType(id, name);
		}
		
		throw new IllegalArgumentException("Unknown value type :"+valueType);
	}
	
	public static LinkType toLinkType(CodelistLink codelistLink) {
		return new LinkType(codelistLink.id(), ValueUtils.safeValue(codelistLink.name()));
	}
	
	private static UIValueFunction toValueFunction(ValueFunction valueFunction) {
		switch (valueFunction.name()) {
			case "identity": return new UIValueFunction(Function.IDENTITY, EMPTY_ARGUMENTS);
			case "uppercase": return new UIValueFunction(Function.UPPERCASE, EMPTY_ARGUMENTS);
			case "lowercase": return new UIValueFunction(Function.LOWERCASE, EMPTY_ARGUMENTS);
			case "prefix": return new UIValueFunction(Function.PREFIX, singleArgument(((PrefixFunction)valueFunction).prefix()));
			case "suffix": return new UIValueFunction(Function.SUFFIX, singleArgument(((SuffixFunction)valueFunction).suffix()));
			case "generic": return new UIValueFunction(Function.CUSTOM, singleArgument(((CustomFunction)valueFunction).expression()));
		}
		throw new IllegalArgumentException("Unknown value function "+valueFunction);
		
	}
	
	private static List<String> singleArgument(String argument) {
		ArrayList<String> arguments = new ArrayList<>();
		arguments.add(argument);
		return arguments;
	}
}
