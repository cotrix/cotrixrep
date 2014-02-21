package org.cotrix.web.publish.client.wizard.step.csvmapping;

import java.util.List;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.publish.client.util.AttributeMappingPanel.DefinitionWidgetProvider;
import org.cotrix.web.publish.client.util.MappingPanel;
import org.cotrix.web.publish.client.util.MappingPanel.ReloadButtonHandler;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.Column;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvMappingStepViewImpl extends ResizeComposite implements CsvMappingStepView, ReloadButtonHandler {

	public static final DefinitionWidgetProvider<Column> PROVIDER = new DefinitionWidgetProvider<Column>() {

		@Override
		public Widget getWidget(Column mapping) {
			TextBox nameField = new TextBox();
			nameField.setStyleName(CommonResources.INSTANCE.css().textBox());
			nameField.setWidth("200px");
			nameField.getElement().getStyle().setPaddingLeft(5, Unit.PX);
			nameField.setValue(mapping.getName());
			return nameField;
		}

		@Override
		public void include(Widget widget, boolean include) {
			((TextBox)widget).setEnabled(include);
		}

		@Override
		public Column getMapping(Widget widget) {
			String name = ((TextBox)widget).getValue();
			Column column = new Column();
			column.setName(name);
			return column;
		}

	};
	

	@UiTemplate("CsvMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, CsvMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);

	@UiField(provided = true) MappingPanel<Column> mappingPanel;

	protected Presenter presenter;

	public CsvMappingStepViewImpl() {
		mappingPanel = new MappingPanel<Column>(PROVIDER, "COLUMNS");
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
	public void setCsvName(String name) {
		mappingPanel.setName(name);
	}

	@Override
	public String getCsvName() {
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

	public void setMappingLoading()
	{
		mappingPanel.setMappingLoading();
	}

	public void unsetMappingLoading()
	{
		mappingPanel.unsetMappingLoading();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setMappings(List<AttributeMapping> mapping)
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

	public void alert(String message) {
		AlertDialog.INSTANCE.center(message);
	}

	@Override
	public void onReloadButtonClicked() {
		presenter.onReload();
	}

}
