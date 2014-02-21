package org.cotrix.web.publish.client.wizard.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.common.client.widgets.EnumListBox;
import org.cotrix.web.common.client.widgets.EnumListBox.LabelProvider;
import org.cotrix.web.publish.client.util.MappingPanel;
import org.cotrix.web.publish.client.util.AttributeMappingPanel.DefinitionWidgetProvider;
import org.cotrix.web.publish.client.util.MappingPanel.ReloadButtonHandler;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.UISdmxElement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SdmxMappingStepViewImpl extends ResizeComposite implements SdmxMappingStepView, ReloadButtonHandler {
	
	protected static final LabelProvider<UISdmxElement> LABEL_PROVIDER = new LabelProvider<UISdmxElement>() {

		@Override
		public String getLabel(UISdmxElement item) {
			return item.getLabel();
		}
	};
	
	public static final DefinitionWidgetProvider<UISdmxElement> PROVIDER = new DefinitionWidgetProvider<UISdmxElement>() {

		@Override
		public Widget getWidget(UISdmxElement mapping) {
			EnumListBox<UISdmxElement> listBox = new EnumListBox<UISdmxElement>(UISdmxElement.class, LABEL_PROVIDER);
			listBox.setStyleName(CommonResources.INSTANCE.css().listBox());
			listBox.setWidth("200px");
			listBox.getElement().getStyle().setPaddingLeft(5, Unit.PX);
			listBox.setSelectedValue(mapping);
			return listBox;
		}

		@Override
		public void include(Widget widget, boolean include) {
			((EnumListBox<?>)widget).setEnabled(include);
		}

		@SuppressWarnings("unchecked")
		@Override
		public UISdmxElement getMapping(Widget widget) {
			return ((EnumListBox<UISdmxElement>)widget).getSelectedValue();
		}

	};

	@UiTemplate("SdmxMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, SdmxMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);
	
	@UiField(provided = true) MappingPanel<UISdmxElement> mappingPanel;
	
	protected Presenter presenter;

	public SdmxMappingStepViewImpl() {
		mappingPanel = new MappingPanel<UISdmxElement>(PROVIDER, "ELEMENTS");
		mappingPanel.setReloadHandler(this);
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	@Override
	public void showMetadata(boolean visible) {
		mappingPanel.showMetadata(visible);
	}
	
	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setCodelistName(String name) {
		mappingPanel.setName(name);
	}

	@Override
	public String getCodelistName() {
		return mappingPanel.getName();
	}
	
	@Override
	public void setVersion(String version) {
		mappingPanel.setVersion(version);
	}

	@Override
	public String getVersion() {
		return mappingPanel.getVersion();
	}
	
	@Override
	public void setSealed(boolean sealed)
	{
		mappingPanel.setSealed(sealed);
	}
	
	@Override
	public boolean getSealed()
	{
		return mappingPanel.getSealed();
	}

	public void setMappings(List<AttributeMapping> mappings)
	{
		mappingPanel.setMapping(mappings);
	}
	
	public void setMappingLoading()
	{
		mappingPanel.setMappingLoading();
	}
	
	public void unsetMappingLoading()
	{
		mappingPanel.unsetMappingLoading();
	}
	
	public List<AttributeMapping> getMappings()
	{
		return mappingPanel.getMappings();
	}

	public void alert(String message) {
		AlertDialog.INSTANCE.center(message);
	}

	@Override
	public void onReloadButtonClicked() {
		presenter.onReload();
	}

}
