/**
 * 
 */
package org.cotrix.web.importwizard.client.step.preview;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfigurationDialog extends DialogBox {

	private static CsvParserConfigurationDialogUiBinder uiBinder = GWT.create(CsvParserConfigurationDialogUiBinder.class);

	interface CsvParserConfigurationDialogUiBinder extends
	UiBinder<Widget, CsvParserConfigurationDialog> {
	}

	interface Style extends CssResource {
		String headerlabel();
		String valuelabel();
	}

	@UiField Button saveButton;
	@UiField Button cancelButton;
	@UiField Style style;

	public CsvParserConfigurationDialog() {
		setModal(true);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		setWidth("500px");

		/*Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				int left = Window.getScrollLeft()+ ((Window.getClientWidth( ) - box.getOffsetWidth( )) / 2); 
				int top = Window.getScrollTop()+((Window.getClientHeight( ) - box.getOffsetHeight( )) / 4);
				box.setPopupPosition(left, top);
			}
		});*/
		setWidget(uiBinder.createAndBindUi(this));

	}
}
