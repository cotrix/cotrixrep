package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.common.client.factory.UIFactories;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributePanel;
import org.cotrix.web.manage.client.codelist.common.attribute.CodelistAttributeEditingPanelFactory;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.data.MetadataProvider;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributesPanel extends LoadingPanel implements HasEditing {

	interface MetadataPanelUiBinder extends UiBinder<Widget, AttributesPanel> {}
	
	private static MetadataPanelUiBinder uiBinder = GWT.create(MetadataPanelUiBinder.class);

	@UiField(provided=true) ItemsEditingPanel<UIAttribute, AttributePanel> attributesGrid;

	@UiField ItemToolbar toolBar;

	protected UICodelistMetadata metadata;

	@Inject
	protected MetadataProvider dataProvider;

	protected DataEditor<UIAttribute> attributeEditor;

	@Inject
	protected CotrixManagerResources resources;

	@Inject
	protected CodelistAttributeEditingPanelFactory editingPanelFactory;
	
	@Inject
	private UIFactories factories;

	@Inject
	public void init() {

		attributesGrid = new ItemsEditingPanel<UIAttribute, AttributePanel>("Codelist attributes", "No attributes", editingPanelFactory);

		attributeEditor = DataEditor.build(this);

		add(uiBinder.createAndBindUi(this));

		toolBar.addButtonClickedHandler(new ButtonClickedHandler() {

			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: addNewAttribute(); break;
					case MINUS: removeSelectedAttribute(); break;
				}
			}
		});

		attributesGrid.setListener(new ItemsEditingListener<UIAttribute>() {

			@Override
			public void onUpdate(UIAttribute item) {
				Log.trace("updated attribute "+item);
				attributeEditor.updated(item);
			}

			@Override
			public void onSwitch(UIAttribute item, SwitchState state) {
			}

			@Override
			public void onCreate(UIAttribute item) {
				attributeEditor.added(item);
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
		if (metadata!=null) {
			UIAttribute attribute = factories.createAttribute();
			attributesGrid.addNewItemPanel(attribute);
		}

	}

	protected void removeSelectedAttribute()
	{
		if (metadata!=null && attributesGrid.getSelectedItem()!=null) {
			UIAttribute selectedAttribute = attributesGrid.getSelectedItem();
			if (Attributes.isSystemAttribute(selectedAttribute)) return; 
			attributesGrid.removeItem(selectedAttribute);
			attributeEditor.removed(selectedAttribute);
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

		if (metadata == null) loadData();
	}

	public void loadData()
	{
		showLoader();
		dataProvider.getData(new AsyncCallback<UICodelistMetadata>() {

			@Override
			public void onSuccess(UICodelistMetadata result) {
				Log.trace("retrieved metadata: "+result);
				setMetadata(result);
				hideLoader();
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading metadata", caught);
				hideLoader();
			}
		});
	}

	protected void setMetadata(UICodelistMetadata metadata)
	{
		this.metadata = metadata;

		Attributes.sortByAttributeType(metadata.getAttributes());

		attributesGrid.clear();
		for (UIAttribute attribute:metadata.getAttributes()) {
			attributesGrid.addItemPanel(attribute);
		}
	}

	@Override
	public void setEditable(boolean editable) {
		attributesGrid.setEditable(editable);
	}
}
