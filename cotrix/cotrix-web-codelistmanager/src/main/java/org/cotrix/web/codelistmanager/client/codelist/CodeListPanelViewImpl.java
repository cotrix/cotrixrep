package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinInjector;
import org.cotrix.web.codelistmanager.client.codelist.CodeListToolbar.ClickListener;
import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListPanelViewImpl extends ResizeComposite implements CodeListPanelView {

	@UiTemplate("CodeListPanel.ui.xml")
	interface CodeListPanelUiBinder extends
	UiBinder<Widget, CodeListPanelViewImpl> {
	}

	private static CodeListPanelUiBinder uiBinder = GWT.create(CodeListPanelUiBinder.class);

	CotrixManagerResources resources = GWT.create(CotrixManagerResources.class);
	
	@UiField DockLayoutPanel contentPanel;
	@UiField CodeListToolbar toolbar;
	@UiField DeckLayoutPanel content;
	@UiField CodeListEditor editor;
	@UiField CodeListMetadataPanel metadata;
	@UiField CodeListAttributesPanel attributes;
	
	@Inject
	public CodeListPanelViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		content.showWidget(editor);
		toolbar.setClickListener(new ClickListener() {
			
			@Override
			public void onClick() {
				switchContent();
				
			}
		});
		metadata.loadData();
	}
	
	@UiFactory
	protected CodeListEditor createEditor()
	{
		return CotrixManagerAppGinInjector.INSTANCE.getCodeListEditor();
	}
	
	@UiFactory
	protected CodeListAttributesPanel createAttributesPanel()
	{
		return CotrixManagerAppGinInjector.INSTANCE.getCodeListAttributesPanel();
	}
	
	@UiFactory
	protected CodeListMetadataPanel getMetadataPanel()
	{
		return CotrixManagerAppGinInjector.INSTANCE.getCodeListMetadataPanel();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		//workaround issuse #7188 https://code.google.com/p/google-web-toolkit/issues/detail?id=7188
		onResize();
	}
	
	protected void switchContent()
	{
		if (content.getVisibleWidget()==editor) content.showWidget(metadata);
		else content.showWidget(editor);
	}

	@Override
	public CodeListToolbar getToolBar()
	{
		return toolbar;
	}
}
