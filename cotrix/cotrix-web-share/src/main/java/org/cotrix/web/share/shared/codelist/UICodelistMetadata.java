/**
 * 
 */
package org.cotrix.web.share.shared.codelist;

import java.util.List;


import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UICodelistMetadata implements IsSerializable {

	protected String id;
	protected UIQName name;
	protected String version;
	protected LifecycleState state;
	protected List<UIAttribute> attributes;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public UIQName getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(UIQName name) {
		this.name = name;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the state
	 */
	public LifecycleState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(LifecycleState state) {
		this.state = state;
	}

	/**
	 * @return the attributes
	 */
	public List<UIAttribute> getAttributes() {
		return attributes;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UICodelistMetadata [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", version=");
		builder.append(version);
		builder.append(", state=");
		builder.append(state);
		builder.append(", attributes=");
		builder.append(attributes);
		builder.append("]");
		return builder.toString();
	}
}
