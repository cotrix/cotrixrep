package org.cotrix.web.manage.client;

import org.cotrix.web.common.client.CommonGinModule;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.CodelistPanelPresenter;
import org.cotrix.web.manage.client.codelists.CodelistsPresenter;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.manager.CodelistManagerPresenter;

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

	public ManageServiceAsync getRpcService();
	public CotrixManageController getController();
	public CodelistsPresenter getCodeListPresenter();
	public CodelistManagerPresenter getCodeListManagerPresenter();
	
	public CodelistPanelPresenter getCodeListPanelPresenter();
	
	@EditorBus
	public EventBus getEditorBus();
}
