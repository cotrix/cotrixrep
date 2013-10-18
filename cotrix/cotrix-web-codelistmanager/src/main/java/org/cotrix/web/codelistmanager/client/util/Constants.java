/**
 * 
 */
package org.cotrix.web.codelistmanager.client.util;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Constants {
	
	protected String defaultNamespace = "http://cotrix.org";
	protected String defaultAttributeType = "description";
	protected String defaultAttributeName = "attribute";
	protected String defaultAttributeValue = "value";
	
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
	

}
