package org.cotrix.web.importwizard.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.resources.Resources;
import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.client.util.MappingPanel;
import org.cotrix.web.importwizard.shared.AttributeMapping;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SdmxMappingStepViewImpl extends Composite implements SdmxMappingStepView {

	@UiTemplate("SdmxMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, SdmxMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);
	
	@UiField TextBox name;
	
	@UiField(provided=true)
	MappingPanel mappingPanel;

	private AlertDialog alertDialog;
	
	protected Presenter presenter;

	public SdmxMappingStepViewImpl() {
		mappingPanel = new MappingPanel(false);
		initWidget(uiBinder.createAndBindUi(this));
		Resources.INSTANCE.css().ensureInjected();
	}
	
	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setCodelistName(String name) {
		this.name.setValue(name);
	}

	@Override
	public String getCodelistName() {
		return this.name.getValue();
	}
	
	@UiHandler("reloadButton")
	protected void reload(ClickEvent clickEvent)
	{
		presenter.onReload();
	}

	public void setMappings(List<AttributeMapping> mappings)
	{
		mappingPanel.setMapping(mappings);
	}
	
	public void setMappingLoading()
	{
		mappingPanel.setLoading();
	}
	
	public void unsetMappingLoading()
	{
		mappingPanel.unsetLoading();
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
		alertDialog.show();
	}

}
