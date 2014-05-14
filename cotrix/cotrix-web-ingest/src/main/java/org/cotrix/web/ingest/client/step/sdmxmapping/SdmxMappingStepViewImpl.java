package org.cotrix.web.ingest.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.ingest.client.util.MappingPanel;
import org.cotrix.web.ingest.client.util.MappingPanel.ReloadButtonHandler;
import org.cotrix.web.ingest.shared.AttributeMapping;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class SdmxMappingStepViewImpl extends ResizeComposite implements SdmxMappingStepView, ReloadButtonHandler {

	@UiTemplate("SdmxMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, SdmxMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);
	
	@UiField(provided = true) MappingPanel mappingPanel;
	
	protected Presenter presenter;
	
	@Inject
	AlertDialog alertDialog;

	public SdmxMappingStepViewImpl() {
		mappingPanel = new MappingPanel(false, "SDMX ELEMENTS");
		mappingPanel.setReloadHandler(this);
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) mappingPanel.resetScroll();
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
		alertDialog.center(message);
	}

	@Override
	public void onReloadButtonClicked() {
		presenter.onReload();
	}

}
