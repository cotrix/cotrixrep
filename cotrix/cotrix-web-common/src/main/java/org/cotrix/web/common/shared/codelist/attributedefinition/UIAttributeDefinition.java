/**
 * 
 */
package org.cotrix.web.common.shared.codelist.attributedefinition;

import java.util.List;
import java.util.Set;

import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.Definition;
import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.common.shared.codelist.UIFacet;
import org.cotrix.web.common.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIAttributeDefinition implements Definition, Identifiable {
	
	private String id;
	private UIQName name;
	private UIQName type;
	private Language language;
	private UIRange range;
	private String defaultValue;
	private List<UIConstraint> constraints;
	private String expression;
	private Set<UIFacet> facets;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UIQName getName() {
		return name;
	}

	public void setName(UIQName name) {
		this.name = name;
	}

	public UIQName getType() {
		return type;
	}

	public void setType(UIQName type) {
		this.type = type;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public UIRange getRange() {
		return range;
	}

	public void setRange(UIRange range) {
		this.range = range;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<UIConstraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<UIConstraint> constraints) {
		this.constraints = constraints;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Set<UIFacet> getFacets() {
		return facets;
	}

	public void setFacets(Set<UIFacet> facets) {
		this.facets = facets;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UIAttributeDefinition [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", language=");
		builder.append(language);
		builder.append(", range=");
		builder.append(range);
		builder.append(", defaultValue=");
		builder.append(defaultValue);
		builder.append(", constraints=");
		builder.append(constraints);
		builder.append(", expression=");
		builder.append(expression);
		builder.append(", facets=");
		builder.append(facets);
		builder.append("]");
		return builder.toString();
	}
}
