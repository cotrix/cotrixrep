package org.cotrix.web.publish.client.util;

import org.cotrix.web.publish.client.util.DefinitionsMappingPanel.DefinitionWidgetProvider;
import org.cotrix.web.publish.shared.DefinitionMapping.MappingTarget;
import org.cotrix.web.publish.shared.DefinitionsMappings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingPanel<T extends MappingTarget> extends ResizeComposite {

	@SuppressWarnings("rawtypes")
	@UiTemplate("MappingPanel.ui.xml")
	interface MappingPanelUiBinder extends UiBinder<Widget, MappingPanel> {}
	private static MappingPanelUiBinder uiBinder = GWT.create(MappingPanelUiBinder.class);
	
	public interface ReloadButtonHandler {
		public void onReloadButtonClicked();
	}
	
	@UiField ScrollPanel scrollMappingPanel;
	@UiField TableRowElement nameRow;
	@UiField TableRowElement versionRow;
	@UiField TableRowElement sealedRow;
	
	@UiField TextBox name;
	@UiField TextBox version;
	@UiField SimpleCheckBox sealed;
	
	@UiField(provided=true)
	DefinitionsMappingPanel<T> codelistMappingPanel;
	
	@UiField(provided=true)
	DefinitionsMappingPanel<T> codeMappingPanel;
	
	@UiField
	TableRowElement codelistMappingRow;
	
	protected ReloadButtonHandler reloadHandler;
	
	public MappingPanel(DefinitionWidgetProvider<T> widgetProvider, String attributeMappingLabel) {
		this(widgetProvider,attributeMappingLabel,true);
	}

	public MappingPanel(DefinitionWidgetProvider<T> widgetProvider, String attributeMappingLabel, boolean includeMappingColumn) {
		codelistMappingPanel = new DefinitionsMappingPanel<T>(widgetProvider, "CODELIST ATTRIBUTES", attributeMappingLabel, includeMappingColumn);
		codeMappingPanel = new DefinitionsMappingPanel<T>(widgetProvider, "CODE ATTRIBUTES & LINKS", attributeMappingLabel, includeMappingColumn);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void showMetadata(boolean visible)
	{
		String value = visible?"table-row":"none";
		nameRow.getStyle().setProperty("display", value);
		versionRow.getStyle().setProperty("display", value);
		sealedRow.getStyle().setProperty("display", value);
	}
	
	public void resetScroll() {
		scrollMappingPanel.scrollToTop();
		scrollMappingPanel.scrollToLeft();
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
		codelistMappingPanel.setLoading();
		codeMappingPanel.setLoading();
	}
	
	public void unsetMappingLoading()
	{
		codelistMappingPanel.unsetLoading();
		codeMappingPanel.unsetLoading();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setMapping(DefinitionsMappings mapping)
	{
		setCodelistMappingRowVisible(!mapping.getCodelistAttributesMapping().isEmpty());
		codelistMappingPanel.setMappings(mapping.getCodelistAttributesMapping());
		codeMappingPanel.setMappings(mapping.getCodesAttributesMapping());
	}
	
	private void setCodelistMappingRowVisible(boolean visible) {
		String value = visible?"table-row":"none";
		codelistMappingRow.getStyle().setProperty("display", value);
	}

	public void setCodeTypeError()
	{
		codelistMappingPanel.setCodeTypeError();
		codeMappingPanel.setCodeTypeError();
	}

	public void cleanStyle()
	{
		codelistMappingPanel.cleanStyle();
		codeMappingPanel.cleanStyle();
	}

	public DefinitionsMappings getMappings()
	{
		return new DefinitionsMappings(codeMappingPanel.getMappings(), codelistMappingPanel.getMappings());
	}
}
