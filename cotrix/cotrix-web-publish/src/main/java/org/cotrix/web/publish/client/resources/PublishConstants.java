/**
 * 
 */
package org.cotrix.web.publish.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishConstants extends Constants {
	
	public static PublishConstants INSTANCE = GWT.create(PublishConstants.class);
	
	@DefaultStringArrayValue({"en","fr"})
	String[] languages();
	
	@DefaultIntValue(30)
	int fileNameMaxSize();

}
