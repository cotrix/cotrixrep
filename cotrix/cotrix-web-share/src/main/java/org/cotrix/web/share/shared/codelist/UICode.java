/**
 * 
 */
package org.cotrix.web.share.shared.codelist;

import java.util.ArrayList;
import java.util.List;



import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UICode implements Identifiable, IsSerializable {
	
	protected String id;
	protected UIQName name;

	protected List<UIAttribute> attributes;

	public UICode(){}
	
	/**
	 * @param id
	 * @param name
	 */
	public UICode(String id, UIQName name) {
		this.id = id;
		this.name = name;
		this.attributes = new ArrayList<UIAttribute>();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public UIQName getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(UIQName name) {
		this.name = name;
	}

	public List<UIAttribute> getAttributes()
	{
		return attributes;
	}
	
	public void addAttribute(UIAttribute attribute)
	{
		attributes.add(attribute);
	}
	
	public void removeAttribute(UIAttribute attribute)
	{
		attributes.remove(attribute);
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<UIAttribute> attributes) {
		this.attributes = attributes;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UICode other = (UICode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UICodeListRow [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", attributes=");
		builder.append(attributes);
		builder.append("]");
		return builder.toString();
	}
	
}
