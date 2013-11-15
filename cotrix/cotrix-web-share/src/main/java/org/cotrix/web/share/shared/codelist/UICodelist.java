package org.cotrix.web.share.shared.codelist;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UICodelist implements IsSerializable
{
	public static final String NAME_FIELD = "NAME";
	public static final String VERSION_FIELD = "VERSION";
	public static final String STATE_FIELD = "STATE";
	
	protected String id;
	protected String name;
	protected String version;
	protected LifecycleState state;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UICodelist [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", version=");
		builder.append(version);
		builder.append(", state=");
		builder.append(state);
		builder.append("]");
		return builder.toString();
	}
}
