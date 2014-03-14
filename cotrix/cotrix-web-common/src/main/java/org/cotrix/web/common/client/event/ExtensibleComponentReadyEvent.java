/**
 * 
 */
package org.cotrix.web.common.client.event;

import org.cotrix.web.common.client.ext.HasExtensionArea;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ExtensibleComponentReadyEvent extends GenericEvent {
	
	private String componentName;
	private HasExtensionArea hasExtensionArea;
	
	/**
	 * @param componentName
	 * @param hasExtensionArea
	 */
	public ExtensibleComponentReadyEvent(String componentName,
			HasExtensionArea hasExtensionArea) {
		this.componentName = componentName;
		this.hasExtensionArea = hasExtensionArea;
	}


	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}


	/**
	 * @return the hasExtensionArea
	 */
	public HasExtensionArea getHasExtensionArea() {
		return hasExtensionArea;
	}
}
