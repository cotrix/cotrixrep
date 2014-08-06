package org.cotrix.web.manage.client;

import org.cotrix.web.common.client.CommonGinModule;
import org.cotrix.web.manage.client.codelist.CodelistPanelController;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;
import org.cotrix.web.manage.client.codelist.cache.LinkTypesCache;
import org.cotrix.web.manage.client.di.CodelistBus;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({CotrixManageGinModule.class, CommonGinModule.class})
public interface CotrixManageGinInjector extends Ginjector {

	public static CotrixManageGinInjector INSTANCE = GWT.create(CotrixManageGinInjector.class);

	public CotrixManageController getController();
	
	public CodelistPanelController getCodeListPanelPresenter();
	
	public LinkTypesCache getLinkTypesCache();
	public AttributeDefinitionsCache getAttributeTypesCache();
	
	@CodelistBus
	public EventBus getEditorBus();
	
}
