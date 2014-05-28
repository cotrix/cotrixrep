/**
 * 
 */
package org.cotrix.web.common.client.factory;

import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributetype.UIRange;

import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public interface UIDefaults {
	
	public UIQName defaultType();
	
	public UIRange defaultRange();
	
	public String defaultValue();

}
