package org.cotrix.web.manage.client.codelist.codes;

import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import org.cotrix.web.common.client.factory.UIFactories;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UIFacet;
import org.cotrix.web.manage.client.codelist.codes.attribute.CodeAttributeEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.codes.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.codelist.codes.event.CodeUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.RemoveItemController;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.side.SidePanel;
import org.cotrix.web.manage.client.data.CodeAttribute;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributesPanel extends ResizeComposite implements HasEditing {

	interface AttributesPanelEventBinder extends EventBinder<AttributesPanel> {}
	
	@Inject
	private SidePanel panel;
	
	private ItemsEditingPanel<UIAttribute> attributesGrid;

	@Inject @CodelistBus
	private EventBus codelistBus;

	private UICode visualizedCode;

	private DataEditor<CodeAttribute> attributeEditor;

	@Inject
	private CotrixManagerResources resources;

	@Inject
	private RemoveItemController attributeRemotionController;
	
	@Inject
	private CodeAttributeEditingPanelFactory editingPanelFactory;
	
	@Inject
	private UIFactories factories;

	@Inject
	public void init() {

		this.attributeEditor = DataEditor.build(this);

		attributesGrid = new ItemsEditingPanel<UIAttribute>("no attributes.", editingPanelFactory);
		panel.setContent(attributesGrid);
		
		panel.getToolBar().setButtonResource(ItemButton.MINUS, BLUE_MINUS);
		panel.getToolBar().setButtonResource(ItemButton.PLUS, BLUE_PLUS);
		
		initWidget(panel);
		
		attributesGrid.setListener(new ItemsEditingListener<UIAttribute>() {
			
			@Override
			public void onUpdate(UIAttribute item) {
				Log.trace("updated attribute "+item);
				attributeEditor.updated(new CodeAttribute(visualizedCode, item));
			}
			
			@Override
			public void onSwitch(UIAttribute item, SwitchState state) {
			}
			
			@Override
			public void onCreate(UIAttribute item) {
				visualizedCode.addAttribute(item);
				attributeEditor.added(new CodeAttribute(visualizedCode, item));
			}
		});

		
		panel.getToolBar().addButtonClickedHandler(new ButtonClickedHandler() {

			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: addNewAttribute(); break;
					case MINUS: removeSelectedAttribute(); break;
				}
			}
		});

		attributesGrid.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedAttributeChanged();
			}
		});

		updateBackground();
	}

	@Inject
	protected void bind(@CurrentCodelist String codelistId, FeatureBinder featureBinder)
	{
		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				panel.getToolBar().setEnabled(ItemButton.PLUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_CODELIST);

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				attributeRemotionController.setUserCanEdit(active);
				//we animate only if the user obtain the edit permission
				updateRemoveButtonVisibility(active);
			}
		}, codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		codelistBus.addHandler(DataEditEvent.getType(UICode.class), new DataEditHandler<UICode>() {

			@Override
			public void onDataEdit(DataEditEvent<UICode> event) {
				if (visualizedCode!=null && visualizedCode.equals(event.getData())) {
					switch (event.getEditType()) {
						case UPDATE: updateVisualizedCode(event.getData()); break;
						case REMOVE: clearVisualizedCode(); break;
						default:
					}
				}
			}
		});

		codelistBus.addHandler(DataEditEvent.getType(CodeAttribute.class), new DataEditHandler<CodeAttribute>() {

			@Override
			public void onDataEdit(DataEditEvent<CodeAttribute> event) {
				if (visualizedCode!=null && visualizedCode.equals(event.getData().getCode())) {
					UIAttribute attribute = event.getData().getAttribute();
					if (!attribute.is(UIFacet.VISIBLE)) return; 
					switch (event.getEditType()) {
						case ADD: {
							if (event.getSource() != AttributesPanel.this) {
								attributesGrid.addItemPanel(attribute);
							}
						} break;
						case UPDATE: attributesGrid.synchWithModel(attribute); break;
						default:
					}
				}
			}
		});

	}

	@Inject
	protected void bind(AttributesPanelEventBinder binder) {
		binder.bindEventHandlers(this, codelistBus);
	}

	@EventHandler
	void onCodeSelected(CodeSelectedEvent event) {
		if (event.getCode() == null) clearVisualizedCode();
		else updateVisualizedCode(event.getCode());
	}

	@EventHandler
	void onCodeUpdated(CodeUpdatedEvent event) {
		updateVisualizedCode(event.getCode());
	}

	private void updateRemoveButtonVisibility(boolean animate) {
		panel.getToolBar().setEnabled(ItemButton.MINUS, attributeRemotionController.canRemove(), animate);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		//GWT issue 7188 workaround
		onResize();
	}

	private void addNewAttribute()
	{
		if (visualizedCode!=null) {
			UIAttribute attribute = factories.createAttribute();
			attributesGrid.addNewItemPanel(attribute);
		}
	}

	private void removeSelectedAttribute()
	{
		if (visualizedCode!=null && attributesGrid.getSelectedItem()!=null) {
			UIAttribute selectedAttribute = attributesGrid.getSelectedItem();
			if (Attributes.isSystemAttribute(selectedAttribute)) return; 
			attributesGrid.removeItem(selectedAttribute);
			visualizedCode.removeAttribute(selectedAttribute);
			attributeEditor.removed(new CodeAttribute(visualizedCode, selectedAttribute));
		}
	}

	private void selectedAttributeChanged()
	{
		boolean canBeRemoved = visualizedCode!=null && attributesGrid.getSelectedItem()!=null && !Attributes.isSystemAttribute(attributesGrid.getSelectedItem());	
		attributeRemotionController.setItemCanBeRemoved(canBeRemoved);
		updateRemoveButtonVisibility(false);
	}

	private void updateVisualizedCode(UICode code)
	{
		visualizedCode = code;
		setHeader();
		updateBackground();

		attributesGrid.clear();
		Attributes.sortByAttributeType(visualizedCode.getAttributes());
		for (UIAttribute attribute:visualizedCode.getAttributes()) {
			if (attribute.is(UIFacet.VISIBLE)) attributesGrid.addItemPanel(attribute);
		}
		
		Log.trace("request refresh of "+visualizedCode.getAttributes().size()+" attributes");
	}

	private void clearVisualizedCode()
	{
		visualizedCode = null;
		setHeader();
		updateBackground();

		attributesGrid.clear();
	}

	private void updateBackground()
	{
		setStyleName(CotrixManagerResources.INSTANCE.css().noItemsBackground(), visualizedCode == null || visualizedCode.getAttributes().isEmpty());
	}

	private void setHeader()
	{
		panel.setHeader("Attributes", visualizedCode!=null?visualizedCode.getName().getLocalPart():null, resources.definitions().ICON_BLUE());
	}

	@Override
	public void setEditable(boolean editable) {
		attributesGrid.setEditable(editable);
	}
}
