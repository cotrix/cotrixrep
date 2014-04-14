/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.OptionalClause;
import org.cotrix.domain.links.ValueFunction;
import org.cotrix.domain.links.ValueFunctions;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.linktype.AttributeType;
import org.cotrix.web.common.shared.codelist.linktype.CodeNameType;
import org.cotrix.web.common.shared.codelist.linktype.LinkType;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.common.shared.codelist.linktype.UIValueFunction;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType.UIValueType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
public class ChangesetUtil {

	public static Attribute addAttribute(UIAttribute uiAttribute) {
		return attribute().name(convert(uiAttribute.getName())).value(convert(uiAttribute.getValue()))
				.ofType(convert(uiAttribute.getType())).in(convert(uiAttribute.getLanguage())).build();
	}

	public static Attribute updateAttribute(UIAttribute uiAttribute) {
		return modifyAttribute(uiAttribute.getId()).name(convert(uiAttribute.getName()))
				.value(convert(uiAttribute.getValue())).ofType(convert(uiAttribute.getType()))
				.in(convert(uiAttribute.getLanguage())).build();
	}

	public static Attribute removeAttribute(String id) {
		return deleteAttribute(id);
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

	public static Code updateCode(String id, UIQName name) {
		return modifyCode(id).name(convert(name)).build();
	}

	public static Code removeCode(String id) {
		return deleteCode(id);
	}

	public static Codelist updateCodelist(String id, UIQName name) {
		return modifyCodelist(id).name(convert(name)).build();
	}
	
	public static CodelistLink addCodelistLink(UILinkType linkType, Codelist target) {
		UIValueType valueType = linkType.getValueType();
		
		OptionalClause clause = null;
		
		if (valueType instanceof CodeNameType) {
			clause = listLink().name(convert(linkType.getName())).target(target).anchorToName();
		}
		
		if (valueType instanceof AttributeType) {
			AttributeType attributeType = (AttributeType) valueType;
			Attribute template = toAttribute(attributeType);
			clause = listLink().name(convert(linkType.getName())).target(target).anchorTo(template);
		}
		
		if (valueType instanceof LinkType) {
			LinkType link = (LinkType) valueType;
			CodelistLink codelistLink = findCodelistLink(link.getLinkId(), target.links());
			clause = listLink().name(convert(linkType.getName())).target(target).anchorTo(codelistLink);
		}
		
		if (clause == null)throw new IllegalArgumentException("Unsupported value type "+valueType);
		
		if (linkType.getValueFunction()!=null) {
			ValueFunction function = toValueFunction(linkType.getValueFunction());
			clause =clause.transformWith(function);
		}
		
		return clause.build();
	}
	
	private static ValueFunction toValueFunction(UIValueFunction valueFunction) {
		switch (valueFunction.getFunction()) {
			case IDENTITY: return ValueFunctions.identity;
			case CUSTOM: return ValueFunctions.custom(getFirstArgument(valueFunction.getArguments()));
			case LOWERCASE: return ValueFunctions.lowercase;
			case PREFIX: return ValueFunctions.prefix(getFirstArgument(valueFunction.getArguments()));
			case SUFFIX: return ValueFunctions.suffix(getFirstArgument(valueFunction.getArguments()));
			case UPPERCASE: return ValueFunctions.uppercase;
			default: throw new IllegalArgumentException("unknown function "+valueFunction);
		}
	}
	
	private static String getFirstArgument(List<String> arguments) {
		if (arguments.size()<1) throw new IllegalArgumentException("Missing arguments, found "+arguments.size()+" expected 1");
		return arguments.get(0);
	}

	public static CodelistLink updateCodelistLink(UILinkType linkType, Codelist target) {
		
		UIValueType valueType = linkType.getValueType();
		
		OptionalClause clause = null;
		
		if (valueType instanceof CodeNameType) {
			clause = modifyListLink(linkType.getId()).name(convert(linkType.getName())).anchorToName();
		}
		
		if (valueType instanceof AttributeType) {
			AttributeType attributeType = (AttributeType) valueType;
			Attribute template = toAttribute(attributeType);
			clause = modifyListLink(linkType.getId()).name(convert(linkType.getName())).anchorTo(template);
		}
		
		if (valueType instanceof LinkType) {
			LinkType link = (LinkType) valueType;
			CodelistLink codelistLink = findCodelistLink(link.getLinkId(), target.links());
			clause = modifyListLink(linkType.getId()).name(convert(linkType.getName())).anchorTo(codelistLink);
		}
		
		if (clause == null)throw new IllegalArgumentException("Unsupported value type "+valueType);
		
		if (linkType.getValueFunction()!=null) {
			ValueFunction function = toValueFunction(linkType.getValueFunction());
			clause = clause.transformWith(function);
		}
		
		return clause.build();
	}
	
	private static Attribute toAttribute(AttributeType attributeType) {
		return attribute().name(convert(attributeType.getName()))
				.ofType(convert(attributeType.getType())).in(convert(attributeType.getLanguage())).build();
	}
	
	private static CodelistLink findCodelistLink(String id, NamedContainer<? extends CodelistLink> namedContainer) {
		for (CodelistLink codelistLink:namedContainer) if (codelistLink.id().equals(id)) return codelistLink;
		throw new IllegalArgumentException("Unknown codelist type with id: "+id);
	}

	public static CodelistLink removeCodelistLink(String id) {
		return deleteListLink(id);
	}

	public static String convert(String value) {
		return (value == null || value.isEmpty()) ? null : value;
	}

	public static QName convert(UIQName value) {
		return (value == null || value.getLocalPart() == null || value.getLocalPart().isEmpty()) ? null : new QName(
				value.getNamespace(), value.getLocalPart());
	}
}
