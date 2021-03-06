package org.cotrix.web.manage.client.codelist.codes;

import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.codelist.codes.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.codelist.codes.event.CodeUpdatedEvent;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerTypeUtil;
import org.cotrix.web.manage.client.codelist.codes.marker.panel.MarkersEditingPanel;
import org.cotrix.web.manage.client.codelist.codes.marker.panel.MarkersEditingPanel.MarkersEditingPanelListener;
import org.cotrix.web.manage.client.codelist.codes.marker.panel.MarkersEditingPanel.SwitchState;
import org.cotrix.web.manage.client.codelist.common.RemoveItemController;
import org.cotrix.web.manage.client.codelist.common.side.SidePanel;
import org.cotrix.web.manage.client.data.CodeAttribute;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkersPanel extends ResizeComposite implements HasEditing {

	interface MarkersPanelEventBinder extends EventBinder<MarkersPanel> {}

	@Inject
	private SidePanel panel;

	@Inject
	private MarkersEditingPanel markersPanel;

	@Inject @CodelistBus
	private EventBus codelistBus;

	private UICode visualizedCode;

	private DataEditor<CodeAttribute> attributeEditor;

	@Inject
	private CotrixManagerResources resources;

	@Inject
	private RemoveItemController markerRemotionController;

	@Inject
	private MarkerTypeUtil markerTypeResolver;

	@Inject
	public void init() {

		this.attributeEditor = DataEditor.build(this);
		panel.setContent(markersPanel);
		
		panel.getToolBar().setVisible(ItemButton.MINUS, false);
		panel.getToolBar().setVisible(ItemButton.PLUS, false);
		
		initWidget(panel);

		markersPanel.setListener(new MarkersEditingPanelListener() {

			@Override
			public void onMarkerUpdate(MarkerType type, String description) {
				markerDescriptionUpdated(type, description);
			}

			@Override
			public void onMarkerSwitch(MarkerType type, UIAttribute attribute, SwitchState state) {
			}

			@Override
			public void onMarkerActived(MarkerType type, String description) {
				markerActived(type, description);
			}

			@Override
			public void onMarkerUnactived(MarkerType type, UIAttribute attribute) {
				markerUnactived(type, attribute);				
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
				markersPanel.setEditable(active);
			}
		}, codelistId, ManagerUIFeature.EDIT_CODELIST);

		codelistBus.addHandler(DataEditEvent.getType(UICode.class), new DataEditHandler<UICode>() {

			@Override
			public void onDataEdit(DataEditEvent<UICode> event) {
				if (visualizedCode!=null && visualizedCode.equals(event.getData())) {
					switch (event.getEditType()) {
						case UPDATE: setVisualizedCode(event.getData()); break;
						case REMOVE: clearVisualizedCode(); break;
						default:
					}
				}
			}
		});

		codelistBus.addHandler(DataEditEvent.getType(CodeAttribute.class), new DataEditHandler<CodeAttribute>() {

			@SuppressWarnings("incomplete-switch")
			@Override
			public void onDataEdit(DataEditEvent<CodeAttribute> event) {
				if (event.getSource() != MarkersPanel.this && visualizedCode!=null && visualizedCode.equals(event.getData().getCode())) {
					UIAttribute attribute = event.getData().getAttribute();
					MarkerType markerType = markerTypeResolver.resolve(attribute);
					if (markerType!=null) {
						switch (event.getEditType()) {
							case ADD: markersPanel.setMarkerActive(markerType, attribute); break;
							case REMOVE: markersPanel.setMarkerUnactive(markerType); break;
						}
					}
				}
			}
		});

	}

	@Inject
	protected void bind(MarkersPanelEventBinder binder) {
		binder.bindEventHandlers(this, codelistBus);
	}

	@EventHandler
	void onCodeSelected(CodeSelectedEvent event) {
		if (event.getCode() == null) clearVisualizedCode();
		else setVisualizedCode(event.getCode());
	}

	@EventHandler
	void onCodeUpdated(CodeUpdatedEvent event) {
		Log.trace("code updated");
		if (event.getCode().equals(visualizedCode)) setVisualizedCode(event.getCode());
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

	private void markerActived(MarkerType type, String description) {
		UIAttribute attribute = markerTypeResolver.toAttribute(type, description);
		visualizedCode.addAttribute(attribute);
		markersPanel.bind(type, attribute);
		attributeEditor.added(new CodeAttribute(visualizedCode, attribute));
	}

	private void markerUnactived(MarkerType type, UIAttribute attribute) {
		visualizedCode.removeAttribute(attribute);
		markersPanel.unbind(type);
		attributeEditor.removed(new CodeAttribute(visualizedCode, attribute));
	}

	private void markerDescriptionUpdated(MarkerType type, String description) {
		UIAttribute attribute = markerTypeResolver.findAttribute(visualizedCode.getAttributes(), type);
		attribute.setNote(description);
		attributeEditor.updated(new CodeAttribute(visualizedCode, attribute));
	}

	private void setVisualizedCode(UICode code)
	{
		Log.trace("setVisualizedCode code: "+code);
		
		visualizedCode = code;
		setHeader();
		updateBackground();

		markersPanel.showEmpty(false);
		markersPanel.clear();

		for (UIAttribute attribute:code.getAttributes()) {
			MarkerType markerType = markerTypeResolver.resolve(attribute);
			if (markerType!=null) {
				markersPanel.setMarkerActive(markerType, attribute);
				//markersPanel.setMarkerOpen(markerType, true);
			}
		}

		Log.trace("request refresh of "+visualizedCode.getAttributes().size()+" attributes");
	}

	private void clearVisualizedCode()
	{
		visualizedCode = null;
		setHeader();
		updateBackground();

		markersPanel.showEmpty(true);
	}

	private void updateBackground()
	{
		setStyleName(CotrixManagerResources.INSTANCE.css().noItemsBackground(), visualizedCode == null || visualizedCode.getAttributes().isEmpty());
	}

	private void setHeader()
	{
		panel.setHeader("Markers", visualizedCode!=null?visualizedCode.getName().getLocalPart():null, resources.definitions().TASK_GREEN());
	}


	@Override
	public void setEditable(boolean editable) {
		markersPanel.setEditable(editable);
	}
}
