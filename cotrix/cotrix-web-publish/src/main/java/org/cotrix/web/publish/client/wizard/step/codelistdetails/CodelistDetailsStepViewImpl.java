/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step.codelistdetails;

import java.util.List;

import org.cotrix.web.share.client.resources.CommonResources;
import org.cotrix.web.share.shared.codelist.CodelistMetadata;
import org.cotrix.web.share.shared.codelist.UIAttribute;

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
	@UiField FlexTable attributes;

	public CodelistDetailsStepViewImpl() {

		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCodelist(CodelistMetadata codelist)
	{
		name.setText(codelist.getName().getLocalPart());
		version.setText(codelist.getVersion());
		addAttributes(codelist.getAttributes());
	}

	public void addAttributes(List<UIAttribute> attributes)
	{
		this.attributes.removeAllRows();
		details.getRowFormatter().setVisible(ASSET_PROPERTIES_ROW, !attributes.isEmpty());
		
		if (!attributes.isEmpty()) {
			setupHeaders();
			for (UIAttribute property:attributes) addRow(property);
		}
	}

	protected void setupHeaders()
	{
		attributes.getFlexCellFormatter().setColSpan(0, 0, 1);
		attributes.setWidget(0, 0, getHeaderLabel("Name"));
		attributes.setWidget(0, 1, getHeaderLabel("Type"));
		attributes.setWidget(0, 2, getHeaderLabel("Language"));
		attributes.setWidget(0, 3, getHeaderLabel("Value"));
	}

	protected Label getHeaderLabel(String text)
	{
		Label headerLabel = new Label(text);
		headerLabel.setStyleName(CommonResources.INSTANCE.css().propertiesTableHeader());
		return headerLabel;
	}

	protected void addRow(UIAttribute attribute)
	{
		int numRows = attributes.getRowCount();
		attributes.setWidget(numRows, 0, new Label(attribute.getName().getLocalPart()));
		attributes.setWidget(numRows, 1, new Label(attribute.getType().getLocalPart()));
		attributes.setWidget(numRows, 2, new Label(attribute.getLanguage()));
		attributes.setWidget(numRows, 3, new Label(attribute.getValue()));
	}
}
