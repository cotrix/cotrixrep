/**
 * 
 */
package org.cotrix.web.publish.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIDefinition implements IsSerializable {
	
	public enum DefinitionType {ATTRIBUTE_DEFINITION, LINK_DEFINITION}
	
	private String id;
	private DefinitionType definitionType;
	
	private String title;
	private String subTitle;
	
	public UIDefinition(){}
	
	public UIDefinition(String id, DefinitionType definitionType, String title,
			String subTitle) {
		this.id = id;
		this.definitionType = definitionType;
		this.title = title;
		this.subTitle = subTitle;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public DefinitionType getDefinitionType() {
		return definitionType;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getSubTitle() {
		return subTitle;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Definition [id=");
		builder.append(id);
		builder.append(", definitionType=");
		builder.append(definitionType);
		builder.append(", title=");
		builder.append(title);
		builder.append(", subTitle=");
		builder.append(subTitle);
		builder.append("]");
		return builder.toString();
	}

}
