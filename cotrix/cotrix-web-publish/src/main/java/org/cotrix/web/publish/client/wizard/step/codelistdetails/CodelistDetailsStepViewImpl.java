/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step.codelistdetails;

import org.cotrix.web.common.client.resources.CommonResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistDetailsStepViewImpl extends ResizeComposite implements CodelistDetailsStepView {
	
	protected static final int ASSET_PROPERTIES_ROW = 3;
	protected static final int REPOSITORY_PROPERTIES_ROW = 3;

	private static CodelistDetailsUiBinder uiBinder = GWT.create(CodelistDetailsUiBinder.class);
	
	@UiTemplate("CodelistDetailsStep.ui.xml")
	interface CodelistDetailsUiBinder extends UiBinder<Widget, CodelistDetailsStepViewImpl> {
	}

	@UiField Grid details;
	@UiField Label name;
	@UiField Label version;
	@UiField Label state;
	@UiField FlexTable attributes;

	public CodelistDetailsStepViewImpl() {

		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setName(String name) {
		this.name.setText(name);
	}
	
	@Override
	public void setVersion(String version) {
		this.version.setText(version);
	}
	
	@Override
	public void setState(String state) {
		this.state.setText(state);
	}
	
	@Override
	public void setAttributesVisible(boolean visible)
	{
		details.getRowFormatter().setVisible(ASSET_PROPERTIES_ROW, visible);
	}
	
	@Override
	public void clearAttributes()
	{
		this.attributes.removeAllRows();
	}
	
	@Override
	public void addAttribute(String name, String value)
	{
		int numRows = attributes.getRowCount();
		attributes.setWidget(numRows, 0, new Label(name));
		attributes.setWidget(numRows, 1, new Label(value));
	}
	

	protected Label getHeaderLabel(String text)
	{
		Label headerLabel = new Label(text);
		headerLabel.setStyleName(CommonResources.INSTANCE.css().propertiesTableHeader());
		return headerLabel;
	}
}
