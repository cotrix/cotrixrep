package org.cotrix.web.manage.client.codelist;

import java.util.List;

import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.linktype.LinkTypesPanel;
import org.cotrix.web.manage.client.codelist.linktype.LinkTypesPanel.LinkTypesPanelListener;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Constants;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistLinkTypesPanel extends LoadingPanel implements HasEditing {

	@UiTemplate("CodelistLinkTypesPanel.ui.xml")
	interface CodelistLinkTypesPanelUiBinder extends UiBinder<Widget, CodelistLinkTypesPanel> {}

	private static CodelistLinkTypesPanelUiBinder uiBinder = GWT.create(CodelistLinkTypesPanelUiBinder.class);

	protected static ImageResourceRenderer renderer = new ImageResourceRenderer(); 

	@Inject
	@UiField(provided=true) LinkTypesPanel linkTypesPanel;

	@UiField ItemToolbar toolBar;

	protected boolean dataLoaded = false;;

	@Inject
	protected ManageServiceAsync service;

	@Inject @CurrentCodelist
	protected String codelistId;

	protected DataEditor<UILinkType> linkTypeEditor;

	@Inject
	protected Constants constants;

	@Inject
	protected CotrixManagerResources resources;

	@Inject
	public void init() {
		linkTypeEditor = DataEditor.build(this);
		add(uiBinder.createAndBindUi(this));
		
		linkTypesPanel.setListener(new LinkTypesPanelListener() {
			
			@Override
			public void onUpdate(UILinkType linkType) {
				linkTypeEditor.updated(linkType);
			}
			
			@Override
			public void onCreate(UILinkType linkType) {
				linkTypeEditor.added(linkType);
			}

			@Override
			public void onSwitch(
					UILinkType item,
					SwitchState state) {
				//ignored				
			}
		});
		
		toolBar.addButtonClickedHandler(new ButtonClickedHandler() {

			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: addNewAttribute(); break;
					case MINUS: removeSelectedAttribute(); break;
				}
			}
		});
	}

	@Inject
	protected void bind(@CurrentCodelist String codelistId)
	{

		FeatureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.PLUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);

		FeatureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.MINUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);
	}

	protected void addNewAttribute()
	{
		linkTypesPanel.addNewLinkType();
	}

	protected void removeSelectedAttribute()
	{
		UILinkType selectedLinkType = linkTypesPanel.getSelectedType();
		if (selectedLinkType!=null) {
			linkTypesPanel.removeLinkType(selectedLinkType);
			linkTypeEditor.removed(selectedLinkType);
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		//workaround issue #7188 https://code.google.com/p/google-web-toolkit/issues/detail?id=7188
		onResize();

		if (!dataLoaded) loadData();
	}

	public void loadData()
	{
		showLoader();
		service.getCodelistLinkTypes(codelistId, new AsyncCallback<DataWindow<UILinkType>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistLinkTypes", caught);
				hideLoader();
			}

			@Override
			public void onSuccess(DataWindow<UILinkType> result) {
				Log.trace("retrieved CodelistLinkTypes: "+result);
				setLinkTypes(result.getData());
				hideLoader();
			}
		});
	}

	protected void setLinkTypes(List<UILinkType> types)
	{
		for (UILinkType linkType:types) linkTypesPanel.addLinkType(linkType);
		dataLoaded = true;
	}

	@Override
	public void setEditable(boolean editable) {
		linkTypesPanel.setEditable(editable);
	}
}
