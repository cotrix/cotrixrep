/**
 * 
 */
package org.cotrix.web.common.shared.codelist.attributetype;

import java.util.List;

import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIAttributeType implements IsSerializable, Identifiable {
	
	private String id;
	private UIQName name;
	private UIQName type;
	private Language language;
	private UIRange range;
	private String defaultValue;
	private List<UIConstraint> constraints;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UIAttributeType [id=");
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
		builder.append("]");
		return builder.toString();
	}
}
