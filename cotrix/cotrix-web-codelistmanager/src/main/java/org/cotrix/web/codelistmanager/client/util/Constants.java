/**
 * 
 */
package org.cotrix.web.codelistmanager.client.util;

import org.cotrix.web.share.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Constants {
	
	protected static String DEFAULT_NAMESPACE = "http://cotrix.org";
	public static final UIQName SYSTEM_TYPE = new UIQName(DEFAULT_NAMESPACE, "system");

	protected String defaultNamespace = DEFAULT_NAMESPACE;
	protected String defaultAttributeType = "description";
	protected String defaultAttributeName = "attribute";
	protected String defaultAttributeValue = "value";
	protected String defaultCodeName = "name";
	
	/**
	 * @return the defaultAttributeName
	 */
	public String getDefaultAttributeName() {
		return defaultAttributeName;
	}

	/**
	 * @param defaultAttributeName the defaultAttributeName to set
	 */
	public void setDefaultAttributeName(String defaultAttributeName) {
		this.defaultAttributeName = defaultAttributeName;
	}

	/**
	 * @return the defaultNamespace
	 */
	public String getDefaultNamespace() {
		return defaultNamespace;
	}
	
	/**
	 * @param defaultNamespace the defaultNamespace to set
	 */
	public void setDefaultNamespace(String defaultNamespace) {
		this.defaultNamespace = defaultNamespace;
	}
	
	/**
	 * @return the defaultAttributeType
	 */
	public String getDefaultAttributeType() {
		return defaultAttributeType;
	}
	
	/**
	 * @param defaultAttributeType the defaultAttributeType to set
	 */
	public void setDefaultAttributeType(String defaultAttributeType) {
		this.defaultAttributeType = defaultAttributeType;
	}

	/**
	 * @return the defaultAttributeValue
	 */
	public String getDefaultAttributeValue() {
		return defaultAttributeValue;
	}

	/**
	 * @param defaultAttributeValue the defaultAttributeValue to set
	 */
	public void setDefaultAttributeValue(String defaultAttributeValue) {
		this.defaultAttributeValue = defaultAttributeValue;
	}

	/**
	 * @return the defaultCodeName
	 */
	public String getDefaultCodeName() {
		return defaultCodeName;
	}
}
