/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.validation.Constraint;
import org.cotrix.domain.validation.Validator;
import org.cotrix.domain.validation.Validators;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIConstraint;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitions {
	
	public static UIAttributeDefinition toUIAttributeType(AttributeDefinition definition) {
		UIAttributeDefinition attributeDefinition = new UIAttributeDefinition();
		attributeDefinition.setId(definition.id());
		attributeDefinition.setLanguage(ValueUtils.safeLanguage(definition.language()));
		attributeDefinition.setName(ValueUtils.safeValue(definition.qname()));
		attributeDefinition.setType(ValueUtils.safeValue(definition.type()));
		attributeDefinition.setRange(toUiRange(definition.range()));
		attributeDefinition.setDefaultValue(definition.valueType().defaultValue());
		attributeDefinition.setConstraints(toUiConstraints(definition.valueType().constraints()));
		attributeDefinition.setExpression(definition.valueType().constraints().asSingleConstraint().expression());
		return attributeDefinition;
	}
	
	private static UIRange toUiRange(Range range) {
		UIRange uiRange = new UIRange();
		uiRange.setMin(range.min());
		uiRange.setMax(range.max());
		return uiRange;
	}
	
	private static List<UIConstraint> toUiConstraints(Iterable<Constraint> constraints) {
		List<UIConstraint> uiConstraints = new ArrayList<>();
		for (Constraint constraint:constraints) uiConstraints.add(toUiConstraint(constraint));
		return uiConstraints;
	}
	
	private static UIConstraint toUiConstraint(Constraint constraint) {
		String name = constraint.name();
		Validator constraintValidator = null;
		for (Validator validator:Validators.values()) {
			if (validator.name().equals(name)) {
				constraintValidator = validator;
				break;
			}
		}
		if (constraintValidator == null) throw new IllegalArgumentException("Validator not found for constraint "+name);
		
		List<String> arguments = new ArrayList<>();
		for (String argName:constraintValidator.parameterNames()) arguments.add(constraint.params().get(argName));
		return new UIConstraint(name, arguments);
	}

}
