/**
 * 
 */
package org.cotrix.web.common.client;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CotrixModuleController {
	
	public CotrixModule getModule();
	
	public void go(final HasWidgets container);
	
	public void activate();
	
	public void deactivate();

}
