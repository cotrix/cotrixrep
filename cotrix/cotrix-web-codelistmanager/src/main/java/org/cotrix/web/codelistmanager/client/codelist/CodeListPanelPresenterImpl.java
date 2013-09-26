package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinInjector;
import org.cotrix.web.codelistmanager.client.data.DataEditor;
import org.cotrix.web.codelistmanager.client.data.MetadataProvider;
import org.cotrix.web.codelistmanager.client.data.MetadataSaver;
import org.cotrix.web.codelistmanager.shared.CodeListMetadata;
import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListPanelPresenterImpl implements CodeListPanelPresenter {

	protected CodeListPanelView view;
	protected UICodelist codelist;

	@Inject
	public CodeListPanelPresenterImpl(CodeListPanelView view) {
		this.view = view;
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	@Override
	public CodeListPanelView getView() {
		return view;
	}
	
	@Override
	public void setCodeList(UICodelist codelist)
	{
		this.codelist = codelist;
		CodeListRowDataProvider provider = CotrixManagerAppGinInjector.INSTANCE.getFactory().createCodeListRowDataProvider(codelist.getId());
		view.setProvider(provider);
		
		MetadataProvider metadataProvider = CotrixManagerAppGinInjector.INSTANCE.getFactory().createMetadataProvider(codelist.getId());
		view.setMetadataProvider(metadataProvider);
		
		DataEditor<CodeListMetadata> editor = new DataEditor<CodeListMetadata>();
		view.setMetadataEditor(editor);
		
		MetadataSaver metadataSaver = CotrixManagerAppGinInjector.INSTANCE.getFactory().createMetadataSaver(codelist.getId());
		editor.addDataEditHandler(metadataSaver);		
		
	}


}
