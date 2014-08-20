/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import static org.cotrix.domain.dsl.Data.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Containers.Attributes;
import org.cotrix.domain.common.Containers.LinkDefinitions;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.common.Ranges;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.ThirdClause;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.utils.Constants;
import org.cotrix.domain.validation.Validator;
import org.cotrix.domain.validation.Validators;
import org.cotrix.domain.values.DefaultType;
import org.cotrix.domain.values.ValueFunction;
import org.cotrix.domain.values.ValueFunctions;
import org.cotrix.domain.values.ValueType;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIConstraint;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;
import org.cotrix.web.common.shared.codelist.linkdefinition.AttributeValue;
import org.cotrix.web.common.shared.codelist.linkdefinition.CodeNameValue;
import org.cotrix.web.common.shared.codelist.linkdefinition.LinkValue;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition.UIValueType;
import org.cotrix.web.common.shared.codelist.linkdefinition.UIValueFunction;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
public class ChangesetUtil {

	public static Attribute addAttribute(UIAttribute uiAttribute, AttributeDefinition definition) {
		
		if (definition!=null) {
			return attribute()
					.instanceOf(definition)
					.value(convert(uiAttribute.getValue()))
					.ofType(convert(uiAttribute.getType()))
					.description(uiAttribute.getNote())
					.build();
		} else {
			return attribute()
					.name(convert(uiAttribute.getName()))
					.value(convert(uiAttribute.getValue()))
					.ofType(convert(uiAttribute.getType()))
					.in(convert(uiAttribute.getLanguage()))
					.description(uiAttribute.getNote())
					.build();		
		}
	}

	public static Attribute updateAttribute(UIAttribute uiAttribute, AttributeDefinition definition) {
		
		if (definition!=null) {
			return modifyAttribute(uiAttribute.getId())
					.instanceOf(definition)
					.value(convert(uiAttribute.getValue()))
					.ofType(convert(uiAttribute.getType()))
					.description(uiAttribute.getNote())
					.build();
		}
		else {
			return modifyAttribute(uiAttribute.getId())
					.name(convert(uiAttribute.getName()))
					.value(convert(uiAttribute.getValue()))
					.ofType(convert(uiAttribute.getType()))
					.in(convert(uiAttribute.getLanguage()))
					.description(uiAttribute.getNote())
					.build();
		}
	}

	public static Attribute removeAttribute(String id) {
		return deleteAttribute(id);
	}

	public static List<Attribute> addAttributes(List<UIAttribute> uiAttributes) {
		if (uiAttributes.size() == 0)
			return Collections.emptyList();
		List<Attribute> attributes = new ArrayList<Attribute>(uiAttributes.size());
		for (UIAttribute uiAttribute : uiAttributes)
			attributes.add(addAttribute(uiAttribute, null /*FIXME */));
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
	
	public static Link addCodelink(UILink link, LinkDefinition linkType, Code code) {
		List<Attribute> attributes = addAttributes(link.getAttributes());
		return link().instanceOf(linkType).target(code).attributes(attributes).build();
	}
	
	public static Link updateCodelink(UILink link, Link oldLink, LinkDefinition linkType, Code code) {
		List<Attribute> attributes = buildChangeSet(link.getAttributes(), oldLink.attributes());
		return modifyLink(link.getId()).target(code).attributes(attributes).build();
	}
	
	public static Link removeCodelink(UILink link) {
		return deleteLink(link.getId());
	}
	
	public static LinkDefinition addCodelistLink(UILinkDefinition linkType, Codelist target) {
		UIValueType valueType = linkType.getValueType();
		
		ThirdClause clause = null;
		
		if (valueType instanceof CodeNameValue) {
			clause = linkdef().name(convert(linkType.getName())).target(target).anchorToName();
		}
		
		if (valueType instanceof AttributeValue) {
			AttributeValue attributeType = (AttributeValue) valueType;
			Attribute template = toAttribute(attributeType);
			clause = linkdef().name(convert(linkType.getName())).target(target).anchorTo(template);
		}
		
		if (valueType instanceof LinkValue) {
			LinkValue link = (LinkValue) valueType;
			LinkDefinition codelistLink = findCodelistLink(link.getLinkId(), target.linkDefinitions());
			clause = linkdef().name(convert(linkType.getName())).target(target).anchorTo(codelistLink);
		}
		
		if (clause == null) throw new IllegalArgumentException("Unsupported value type "+valueType);
		
		List<Attribute> attributes = addAttributes(linkType.getAttributes());
		clause = clause.attributes(attributes);
		
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

	public static LinkDefinition updateCodelistLink(UILinkDefinition linkType, Codelist target, LinkDefinition oldCodelistLink) {
		
		UIValueType valueType = linkType.getValueType();
		
		ThirdClause clause = null;
		
		if (valueType instanceof CodeNameValue) {
			clause = modifyLinkDef(linkType.getId()).name(convert(linkType.getName())).anchorToName();
		}
		
		if (valueType instanceof AttributeValue) {
			AttributeValue attributeType = (AttributeValue) valueType;
			Attribute template = toAttribute(attributeType);
			clause = modifyLinkDef(linkType.getId()).name(convert(linkType.getName())).anchorTo(template);
		}
		
		if (valueType instanceof LinkValue) {
			LinkValue link = (LinkValue) valueType;
			LinkDefinition codelistLink = findCodelistLink(link.getLinkId(), target.linkDefinitions());
			clause = modifyLinkDef(linkType.getId()).name(convert(linkType.getName())).anchorTo(codelistLink);
		}
		
		if (clause == null) throw new IllegalArgumentException("Unsupported value type "+valueType);
		
		List<Attribute> attributes = buildChangeSet(linkType.getAttributes(), oldCodelistLink.attributes());
		clause = clause.attributes(attributes);
		
		if (linkType.getValueFunction()!=null) {
			ValueFunction function = toValueFunction(linkType.getValueFunction());
			clause = clause.transformWith(function);
		}
		
		return clause.build();
	}
	
	private static List<Attribute> buildChangeSet(List<UIAttribute> uiAttributes, Attributes oldAttributes) {
		
		List<Attribute> changeSet = new ArrayList<>();
		
		Set<String> updatedAttributeIds = new HashSet<>();
		
		for (UIAttribute uiAttribute:uiAttributes) {
			
			//skip system attributes
			if (isSystemAttribute(uiAttribute)) continue;
		
			//is a new attribute
			if (uiAttribute.getId() == null) changeSet.add(addAttribute(uiAttribute, null /*FIXME */));
			//is an updated attribute
			else {
				changeSet.add(updateAttribute(uiAttribute, null /*FIXME */));
				updatedAttributeIds.add(uiAttribute.getId());
			}
		}
		
		for (Attribute oldAttribute:oldAttributes) {
			String id = oldAttribute.id();
			if (!updatedAttributeIds.contains(id)) changeSet.add(removeAttribute(id));
		}
				
		return changeSet;
	}
	
	private static boolean isSystemAttribute(UIAttribute attribute) {
		UIQName type = attribute.getType();
		if (type == null) return false;
		
		String nameSpace = attribute.getType().getNamespace();
		String localPart = attribute.getType().getLocalPart();
		if (nameSpace == null || localPart == null) return false;
		
		return Constants.SYSTEM_TYPE.getNamespaceURI().equals(nameSpace) && 
				Constants.SYSTEM_TYPE.getLocalPart().equals(localPart);
	}
	
	private static Attribute toAttribute(AttributeValue attributeType) {
		return attribute().name(convert(attributeType.getName()))
				.ofType(convert(attributeType.getType())).in(convert(attributeType.getLanguage())).build();
	}
	
	private static LinkDefinition findCodelistLink(String id, LinkDefinitions namedContainer) {
		for (LinkDefinition codelistLink:namedContainer) if (codelistLink.id().equals(id)) return codelistLink;
		throw new IllegalArgumentException("Unknown codelist type with id: "+id);
	}
	
	
	public static AttributeDefinition addDefinition(UIAttributeDefinition attributeType) {
		return attrdef().name(convert(attributeType.getName()))
				.is(convert(attributeType.getType()))
				.in(convert(attributeType.getLanguage()))
				.occurs(toRange(attributeType.getRange()))
				.valueIs(toValueType(attributeType.getDefaultValue(), attributeType.getConstraints()))
				.build();
	}
	
	public static AttributeDefinition updateDefinition(UIAttributeDefinition attributeType) {
		return modifyAttrDef(attributeType.getId()).name(convert(attributeType.getName()))
				.is(convert(attributeType.getType()))
				.in(convert(attributeType.getLanguage()))
				.occurs(toRange(attributeType.getRange()))
				.valueIs(toValueType(attributeType.getDefaultValue(), attributeType.getConstraints()))
				.build();
	}
	
	public static Range toRange(UIRange range) {
		return Ranges.between(range.getMin(), range.getMax());
	}
	
	public static ValueType toValueType(String defaultValue, List<UIConstraint> constraints) {
		DefaultType type = valueType();
		
		for (UIConstraint constraint:constraints) addConstraint(type, constraint);

		type.defaultsTo(defaultValue);
		
		return type;
	}
	
	private static void addConstraint(DefaultType type, UIConstraint constraint) {
		Validator constraintValidator = null;
		for (Validator validator:Validators.values()) {
			if (validator.name().equals(constraint.getName())) {
				constraintValidator = validator;
				break;
			}
		}
		if (constraintValidator == null) throw new IllegalArgumentException("Validator for constraint "+constraint+" not found");
		
		type.with(constraintValidator.instance(constraint.getParameters().toArray()));
	}
	
	
	public static String convert(Language language) {
		return (language == null || language == Language.NONE) ? null : language.getCode();
	}

	public static String convert(String value) {
		return (value == null || value.isEmpty()) ? null : value;
	}

	public static QName convert(UIQName value) {
		return (value == null || value.getLocalPart() == null || value.getLocalPart().isEmpty()) ? null : new QName(
				value.getNamespace(), value.getLocalPart());
	}
}
