package org.cotrix.web.manage.client;

import org.cotrix.web.common.client.CommonGinModule;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.CodelistAttributesPanel;
import org.cotrix.web.manage.client.codelist.CodelistEditor;
import org.cotrix.web.manage.client.codelist.CodelistMetadataPanel;
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
	
	public CodelistEditor getCodeListEditor();
	public CodelistAttributesPanel getCodeListAttributesPanel();
	public CodelistMetadataPanel getCodeListMetadataPanel();
	
	@EditorBus
	public EventBus getEditorBus();
}
