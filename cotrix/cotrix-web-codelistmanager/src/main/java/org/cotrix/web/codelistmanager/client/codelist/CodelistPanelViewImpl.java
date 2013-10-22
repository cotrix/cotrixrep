package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinInjector;
import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistPanelViewImpl extends ResizeComposite implements CodelistPanelView {

	@UiTemplate("CodelistPanel.ui.xml")
	interface CodeListPanelUiBinder extends
	UiBinder<Widget, CodelistPanelViewImpl> {
	}

	private static CodeListPanelUiBinder uiBinder = GWT.create(CodeListPanelUiBinder.class);

	CotrixManagerResources resources = GWT.create(CotrixManagerResources.class);
	
	@UiField SplitLayoutPanel mainPanel;
	@UiField DockLayoutPanel contentPanel;
	@UiField CodelistToolbarImpl toolbar;
	@UiField CodelistEditor editor;
	@UiField CodelistSidePanel sidePanel;
	
	@Inject
	public CodelistPanelViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		mainPanel.setWidgetToggleDisplayAllowed(sidePanel, true);
	}
	
	@UiFactory
	protected CodelistEditor createEditor()
	{
		return CotrixManagerAppGinInjector.INSTANCE.getCodeListEditor();
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

	@Override
	public CodelistToolbar getToolBar()
	{
		return toolbar;
	}

	@Override
	public CodelistEditor getCodeListEditor() {
		return editor;
	}
}
