/**
 * 
 */
package org.cotrix.web.codelistmanager.shared;

import java.util.Map;

import org.cotrix.web.share.shared.UIAttribute;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UICodeListRow implements IsSerializable {
	
	protected String id;
	protected String code;
	protected String name;
	protected Map<String, UIAttribute> attributes;

	protected UICodeListRow(){}
	
	/**
	 * @param id
	 * @param code
	 * @param name
	 */
	public UICodeListRow(String id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public Iterable<UIAttribute> getAttributes()
	{
		return attributes.values();
	}
	
	public UIAttribute getAttribute(String name)
	{
		return attributes.get(name);
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, UIAttribute> attributes) {
		this.attributes = attributes;
	}

}
