package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.common.client.factory.UIFactories;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributePanel;
import org.cotrix.web.manage.client.codelist.common.side.SidePanel;
import org.cotrix.web.manage.client.codelist.event.ReadyEvent;
import org.cotrix.web.manage.client.codelist.metadata.attribute.CodelistAttributeEditingPanelFactory;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.data.MetadataProvider;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributesPanel extends LoadingPanel implements HasEditing {
	
	interface AttributesPanelEventBinder extends EventBinder<AttributesPanel> {}

	@Inject
	private SidePanel panel;
	
	private ItemsEditingPanel<UIAttribute, AttributePanel> attributesGrid;

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
	public void init(@CurrentCodelist UICodelist codelist) {

		attributesGrid = new ItemsEditingPanel<UIAttribute, AttributePanel>("No attributes", editingPanelFactory);
		panel.setContent(attributesGrid);

		attributeEditor = DataEditor.build(this);

		add(panel);

		panel.getToolBar().addButtonClickedHandler(new ButtonClickedHandler() {

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
		
		panel.setHeader("Attributes", codelist.getName().getLocalPart(), "#000000");
	}

	@Inject
	protected void bind(@CurrentCodelist String codelistId, FeatureBinder featureBinder)
	{

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				panel.getToolBar().setEnabled(ItemButton.PLUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				panel.getToolBar().setEnabled(ItemButton.MINUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);
	}
	
	@Inject
	protected void bind(@CodelistBus EventBus codelistBus, AttributesPanelEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, codelistBus);
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
	}
	
	@EventHandler
	public void onReady(ReadyEvent event) {
		loadData();
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
