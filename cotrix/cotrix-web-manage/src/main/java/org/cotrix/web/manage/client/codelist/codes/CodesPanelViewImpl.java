package org.cotrix.web.manage.client.codelist.codes;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.manage.client.codelist.codes.editor.CodesEditor;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
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
public class CodesPanelViewImpl extends ResizeComposite implements CodesPanelView {

	@UiTemplate("CodesPanel.ui.xml")
	interface CodesPanelUiBinder extends UiBinder<Widget, CodesPanelViewImpl> {}

	private static CodesPanelUiBinder uiBinder = GWT.create(CodesPanelUiBinder.class);

	CotrixManagerResources resources = GWT.create(CotrixManagerResources.class);
	
	@UiField(provided=true) SplitLayoutPanel mainPanel;
	@UiField DockLayoutPanel contentPanel;
	
	@Inject
	@UiField(provided=true) CodesToolbarImpl toolbar;
	
	@Inject
	@UiField(provided=true) CodesEditor editor;
	
	@Inject
	@UiField(provided=true) CodesSidePanel sidePanel;
	
	@Inject
	protected void init() {
		mainPanel = new SplitLayoutPanel(3);
		initWidget(uiBinder.createAndBindUi(this));
		mainPanel.setWidgetToggleDisplayAllowed(sidePanel, true);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		//workaround issuse #7188 https://code.google.com/p/google-web-toolkit/issues/detail?id=7188
		if (visible) onResize();
	}

	@Override
	public CodesToolbar getToolBar()	{
		return toolbar;
	}

	@Override
	public CodesEditor getCodeListEditor() {
		return editor;
	}

	@Override
	public HasEditing getAttributesEditor() {
		return sidePanel.getAttributesPanel();
	}
	
	@Override
	public HasEditing getLinksEditor() {
		return sidePanel.getLinksPanel();
	}
}
