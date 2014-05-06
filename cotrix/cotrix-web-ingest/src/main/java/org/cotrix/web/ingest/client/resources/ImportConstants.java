/**
 * 
 */
package org.cotrix.web.ingest.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public interface ImportConstants extends Constants {
	
	@Deprecated
	public static ImportConstants INSTANCE = GWT.create(ImportConstants.class);
	
	@DefaultIntValue(30)
	int fileNameMaxSize();
	
	String[] csvMimeTypes();
	String[] xmlMimeTypes();

}
