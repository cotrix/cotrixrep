package org.cotrix.web.publish.client.wizard.step.sdmxmapping;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.widgets.EnumListBox;
import org.cotrix.web.common.client.widgets.EnumListBox.LabelProvider;
import org.cotrix.web.publish.client.util.DefinitionsMappingPanel.DefinitionWidgetProvider;
import org.cotrix.web.publish.client.util.MappingPanel;
import org.cotrix.web.publish.client.util.MappingPanel.ReloadButtonHandler;
import org.cotrix.web.publish.shared.DefinitionsMappings;
import org.cotrix.web.publish.shared.UISdmxElement;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class SdmxMappingStepViewImpl extends ResizeComposite implements SdmxMappingStepView, ReloadButtonHandler {
	
	protected static final LabelProvider<UISdmxElement> LABEL_PROVIDER = new LabelProvider<UISdmxElement>() {

		@Override
		public String getLabel(UISdmxElement item) {
			return item.getLabel();
		}
	};
	
	public static final DefinitionWidgetProvider<UISdmxElement> PROVIDER = new DefinitionWidgetProvider<UISdmxElement>() {

		@Override
		public Widget getTargetWidget(UISdmxElement mapping) {
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
		public UISdmxElement getTarget(Widget widget) {
			return ((EnumListBox<UISdmxElement>)widget).getSelectedValue();
		}

	};
	
	private MappingPanel<UISdmxElement> mappingPanel;
	
	private Presenter presenter;

	public SdmxMappingStepViewImpl() {
		mappingPanel = new MappingPanel<UISdmxElement>(PROVIDER, "SDMX ELEMENTS");
		mappingPanel.setReloadHandler(this);
		
		initWidget(mappingPanel);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) mappingPanel.resetScroll();
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

	public void setMappings(DefinitionsMappings mappings)
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
	
	public DefinitionsMappings getMappings()
	{
		return mappingPanel.getMappings();
	}

	@Override
	public void onReloadButtonClicked() {
		presenter.onReload();
	}

}
