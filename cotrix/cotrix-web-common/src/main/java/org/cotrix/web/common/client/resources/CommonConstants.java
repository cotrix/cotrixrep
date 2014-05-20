/**
 * 
 */
package org.cotrix.web.common.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public interface CommonConstants extends Constants {
	
	public static CommonConstants INSTANCE = GWT.create(CommonConstants.class);
}
