/**
 * 
 */
package org.cotrix.web.common.shared.codelist.link;

import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UILinkType implements Identifiable, IsSerializable {
	
	private String id;
	private UIQName name;
	private UICodelist targetCodelist;
	private String valueFunction;
	private ValueType valueType;
	
	public UILinkType(){}
	
	/**
	 * @param id
	 * @param name
	 * @param targetCodelist
	 * @param valueFunction
	 * @param valueType
	 */
	public UILinkType(String id, UIQName name, UICodelist targetCodelist,
			String valueFunction, ValueType valueType) {
		this.id = id;
		this.name = name;
		this.targetCodelist = targetCodelist;
		this.valueFunction = valueFunction;
		this.valueType = valueType;
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
	 * @return the targetCodelist
	 */
	public UICodelist getTargetCodelist() {
		return targetCodelist;
	}

	/**
	 * @param targetCodelist the targetCodelist to set
	 */
	public void setTargetCodelist(UICodelist targetCodelist) {
		this.targetCodelist = targetCodelist;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}	
	
	/**
	 * @return the valueFunction
	 */
	public String getValueFunction() {
		return valueFunction;
	}

	/**
	 * @param valueFunction the valueFunction to set
	 */
	public void setValueFunction(String valueFunction) {
		this.valueFunction = valueFunction;
	}

	/**
	 * @return the valueType
	 */
	public ValueType getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public interface ValueType {
	}
}
