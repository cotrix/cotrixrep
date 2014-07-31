/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.panel;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Set;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.codes.marker.panel.MarkerPanel.MarkerPanelListener;
import org.cotrix.web.manage.client.codelist.codes.marker.style.DefaultMarkerStyleProvider;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkersEditingPanel extends Composite implements HasEditing {
	
	public static enum SwitchState {DOWN, UP};

	public interface MarkersEditingPanelListener {
		public void onMarkerUpdate(MarkerType type, String description);
		public void onMarkerSwitch(MarkerType type, UIAttribute attribute, SwitchState state);
		public void onMarkerActived(MarkerType type, String description);
		public void onMarkerUnactived(MarkerType type, UIAttribute attribute);
	}


	private VerticalPanel mainPanel;

	private EnumMap<MarkerType, MarkerPanel> panels = new EnumMap<MarkerType, MarkerPanel>(MarkerType.class);
	private EnumMap<MarkerType, UIAttribute> bindings = new EnumMap<MarkerType, UIAttribute>(MarkerType.class);
	private MarkersEditingPanelListener listener;
	
	private HTML emptyWidget;
	private HorizontalPanel emptyWidgetContainer;

	@Inject
	public MarkersEditingPanel(DefaultMarkerStyleProvider styleProvider) {

		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		
		emptyWidget = new HTML("No markers");
		emptyWidget.setStyleName(CotrixManagerResources.INSTANCE.css().noItemsLabel());
		emptyWidgetContainer = new HorizontalPanel();
		emptyWidgetContainer.setWidth("100%");
		emptyWidgetContainer.setHeight("200px");
		emptyWidgetContainer.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		emptyWidgetContainer.add(emptyWidget);
		
		mainPanel.add(emptyWidgetContainer);

		createPanels(styleProvider);
		
		showEmpty(true);

		initWidget(mainPanel);
	}
	
	public void showEmpty(boolean show) {
		emptyWidgetContainer.setVisible(show);
		for (MarkerPanel panel:panels.values()) panel.setVisible(!show);
	}

	private void createPanels(DefaultMarkerStyleProvider styleProvider) {
		for (final MarkerType markerType:MarkerType.values()) {
			MarkerPanel markerPanel = new MarkerPanel(markerType, styleProvider.getStyle(markerType));
			panels.put(markerType, markerPanel);
			mainPanel.add(markerPanel);
			
			markerPanel.setListener(new MarkerPanelListener() {
				
				@Override
				public void onSwitch(boolean isDown) {
					fireSwitch(markerType, isDown);
				}
				
				@Override
				public void onUpdate(String description){
					fireUpdate(markerType, description);
				}

				@Override
				public void onMarkerActived(String description) {
					fireMarkerActived(markerType, description);					
				}

				@Override
				public void onMarkerUnactived() {
					fireMarkerUnactived(markerType);					
				}
			});
		}
	}

	public void setMarkerActive(MarkerType markerType, UIAttribute attribute) {
		panels.get(markerType).setActive(attribute.getDescription(), attribute.getValue());
		bind(markerType, attribute);
	}
	
	public void bind(MarkerType markerType, UIAttribute attribute) {
		bindings.put(markerType, attribute);
	}
	
	public void setMarkerUnactive(MarkerType markerType) {
		panels.get(markerType).setUnactive();
		unbind(markerType);
	}

	public void unbind(MarkerType markerType) {
		bindings.remove(markerType);
	}
	
	public void setSwitchState(MarkerType markerType, SwitchState state) {
		panels.get(markerType).setSwitchDown(state == SwitchState.DOWN);
	}
	
	public void setMarkerOpen(MarkerType markerType, boolean open) {
		panels.get(markerType).openDetails(open);
	}

	public void clear() {
		for (MarkerPanel panel:panels.values()) panel.setUnactive();
		bindings.clear();
	}
	
	public Set<Entry<MarkerType,UIAttribute>> getAttributes() {
		return bindings.entrySet();
	}

	public void setListener(MarkersEditingPanelListener listener) {
		this.listener = listener;
	}

	@Override
	public void setEditable(boolean editable) {
		for (MarkerPanel panel:panels.values()) panel.setEditable(editable);
	}

	private void fireUpdate(MarkerType type, String description) {
		if (listener!=null) listener.onMarkerUpdate(type, description);
	}

	private void fireSwitch(MarkerType type, boolean isDown) {
		if (listener!=null) listener.onMarkerSwitch(type, bindings.get(type), isDown?SwitchState.DOWN:SwitchState.UP);
	}

	private void fireMarkerActived(MarkerType type, String description) {
		if (listener!=null) listener.onMarkerActived(type, description);
	}
	
	private void fireMarkerUnactived(MarkerType type) {
		if (listener!=null) listener.onMarkerUnactived(type, bindings.get(type));
	}
}
