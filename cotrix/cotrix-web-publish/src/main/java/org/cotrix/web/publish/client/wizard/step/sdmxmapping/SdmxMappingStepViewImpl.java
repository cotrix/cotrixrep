package org.cotrix.web.publish.client.wizard.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.publish.client.util.MappingPanel;
import org.cotrix.web.publish.client.util.AttributeMappingPanel.DefinitionWidgetProvider;
import org.cotrix.web.publish.client.util.MappingPanel.ReloadButtonHandler;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.SdmxElement;
import org.cotrix.web.share.client.resources.CommonResources;
import org.cotrix.web.share.client.widgets.AlertDialog;
import org.cotrix.web.share.client.widgets.EnumListBox;
import org.cotrix.web.share.client.widgets.EnumListBox.LabelProvider;

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
	
	protected static final LabelProvider<SdmxElement> LABEL_PROVIDER = new LabelProvider<SdmxElement>() {

		@Override
		public String getLabel(SdmxElement item) {
			return item.getLabel();
		}
	};
	
	public static final DefinitionWidgetProvider<SdmxElement> PROVIDER = new DefinitionWidgetProvider<SdmxElement>() {

		@Override
		public Widget getWidget(SdmxElement mapping) {
			EnumListBox<SdmxElement> listBox = new EnumListBox<SdmxElement>(SdmxElement.class, LABEL_PROVIDER);
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
		public SdmxElement getMapping(Widget widget) {
			return ((EnumListBox<SdmxElement>)widget).getSelectedValue();
		}

	};

	@UiTemplate("SdmxMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, SdmxMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);
	
	@UiField(provided = true) MappingPanel<SdmxElement> mappingPanel;

	private AlertDialog alertDialog;
	
	protected Presenter presenter;

	public SdmxMappingStepViewImpl() {
		mappingPanel = new MappingPanel<SdmxElement>(PROVIDER, "ELEMENTS");
		mappingPanel.setReloadHandler(this);
		
		initWidget(uiBinder.createAndBindUi(this));
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
		if(alertDialog == null){
			alertDialog = new AlertDialog(false);
		}
		alertDialog.setMessage(message);
		alertDialog.center();
	}

	@Override
	public void onReloadButtonClicked() {
		presenter.onReload();
	}

}
