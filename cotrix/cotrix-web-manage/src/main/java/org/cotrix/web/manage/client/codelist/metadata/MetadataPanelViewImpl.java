package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.common.client.widgets.HasEditing;
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
public class MetadataPanelViewImpl extends ResizeComposite implements MetadataPanelView {

	@UiTemplate("MetadataPanel.ui.xml")
	interface MetadataPanelUiBinder extends	UiBinder<Widget, MetadataPanelViewImpl> {}

	private static MetadataPanelUiBinder uiBinder = GWT.create(MetadataPanelUiBinder.class);
	
	@UiField SplitLayoutPanel mainPanel;
	@UiField DockLayoutPanel contentPanel;
	@UiField MetadataToolbarImpl toolbar;
	
	@Inject
	@UiField(provided=true) MetadataSidePanel sidePanel;
	
	@Inject
	@UiField(provided=true) SplashPanel splashPanel;
	
	@Inject
	private CotrixManagerResources resources;
	
	@Inject
	protected void init() {
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
		onResize();
		contentPanel.onResize();
	}

	@Override
	public SplashPanel getSplashPanel() {
		return splashPanel;
	}

	@Override
	public MetadataToolbar getToolBar()	{
		return toolbar;
	}

	@Override
	public HasEditing getAttributesEditor() {
		return sidePanel.getAttributesPanel();
	}

	@Override
	public HasEditing getLinkTypesEditor() {
		return splashPanel.getLinkTypesPanel();
	}
	
	@Override
	public HasEditing getAttributeTypesPanel() {
		return splashPanel.getAttributeTypesPanel();
	}
}
