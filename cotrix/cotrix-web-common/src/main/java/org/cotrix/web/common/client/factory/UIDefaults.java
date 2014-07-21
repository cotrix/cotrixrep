/**
 * 
 */
package org.cotrix.web.common.client.factory;

import java.util.List;

import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;

import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public interface UIDefaults {
	
	public UIQName defaultType();
	
	public List<UIQName> defaultTypes();
	
	public UIRange defaultRange();
	
	public String defaultValue();
	
	public String defaultNameSpace();
	
	public UIQName systemType();

}
