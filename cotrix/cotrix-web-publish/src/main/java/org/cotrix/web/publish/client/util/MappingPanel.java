package org.cotrix.web.publish.client.util;

import java.util.List;

import org.cotrix.web.publish.shared.AttributeMapping;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingPanel extends ResizeComposite {

	@UiTemplate("MappingPanel.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, MappingPanel> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);
	
	public interface ReloadButtonHandler {
		public void onReloadButtonClicked();
	}
	
	@UiField TableRowElement nameRow;
	@UiField TableRowElement versionRow;
	@UiField TableRowElement sealedRow;
	
	@UiField TextBox name;
	@UiField TextBox version;
	@UiField SimpleCheckBox sealed;
	@UiField InlineLabel attributeMappingLabel;
	
	@UiField(provided=true)
	AttributeMappingPanel mappingPanel;
	
	protected ReloadButtonHandler reloadHandler;
	
	public MappingPanel(boolean hasTypeDefinition, String attributeMappingLabel) {
		this(true, hasTypeDefinition, attributeMappingLabel);
	}

	public MappingPanel(boolean showMetadata, boolean hasTypeDefinition, String attributeMappingLabel) {
		mappingPanel = new AttributeMappingPanel(hasTypeDefinition);
		initWidget(uiBinder.createAndBindUi(this));
		this.attributeMappingLabel.setText(attributeMappingLabel);
		if (!showMetadata) hideMetadata();
	}
	
	protected void hideMetadata()
	{
		nameRow.getStyle().setDisplay(Display.NONE);
		versionRow.getStyle().setDisplay(Display.NONE);
		sealedRow.getStyle().setDisplay(Display.NONE);
	}
	
	/**
	 * @param reloadHandler the reloadHandler to set
	 */
	public void setReloadHandler(ReloadButtonHandler reloadHandler) {
		this.reloadHandler = reloadHandler;
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public String getName() {
		return this.name.getValue();
	}

	public void setVersion(String version) {
		this.version.setValue(version);
	}

	public String getVersion() {
		return this.version.getValue();
	}
	
	public void setSealed(boolean sealed)
	{
		this.sealed.setValue(sealed);
	}
	
	public boolean getSealed()
	{
		return sealed.getValue();
	}
	
	@UiHandler("reloadButton")
	protected void reload(ClickEvent clickEvent)
	{
		if (reloadHandler!=null) reloadHandler.onReloadButtonClicked();
	}
	
	public void setMappingLoading()
	{
		mappingPanel.setLoading();
	}
	
	public void unsetMappingLoading()
	{
		mappingPanel.unsetLoading();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setMapping(List<AttributeMapping> mapping)
	{
		mappingPanel.setMapping(mapping);
	}

	public void setCodeTypeError()
	{
		mappingPanel.setCodeTypeError();
	}

	public void cleanStyle()
	{
		mappingPanel.cleanStyle();
	}

	public List<AttributeMapping> getMappings()
	{
		return mappingPanel.getMappings();
	}
}
