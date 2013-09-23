package org.cotrix.web.importwizard.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.util.MappingPanel;
import org.cotrix.web.importwizard.client.util.MappingPanel.ReloadButtonHandler;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.share.client.util.AlertDialog;

import com.google.gwt.core.client.GWT;
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

	@UiTemplate("SdmxMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, SdmxMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);
	
	@UiField(provided = true) MappingPanel mappingPanel;

	private AlertDialog alertDialog;
	
	protected Presenter presenter;

	public SdmxMappingStepViewImpl() {
		mappingPanel = new MappingPanel(false, "ELEMENTS");
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
