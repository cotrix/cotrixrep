package org.cotrix.web.menu.client.presenter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import org.cotrix.web.common.client.CommonGinModule;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({CotrixMenuGinModule.class, CommonGinModule.class})
public interface CotrixMenuGinInjector extends Ginjector {
	
	public static CotrixMenuGinInjector INSTANCE = GWT.create(CotrixMenuGinInjector.class);

	public MenuPresenter getMenuPresenter();
}
