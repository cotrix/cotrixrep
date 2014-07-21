package org.cotrix.web.common.client.widgets.dialog;

import org.cotrix.web.common.client.resources.CommonResources;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(ConfirmDialogImpl.class)
public interface ConfirmDialog {
	
	public interface DialogButton {
		String getLabel();
		String getStyleName();
	}
	
	public enum DialogButtonDefaultSet implements DialogButton {
		CONTINUE("Continue", CommonResources.INSTANCE.css().blueButton()), 
		CANCEL("Cancel", CommonResources.INSTANCE.css().grayButton());
		
		private String label;
		private String styleName;

		private DialogButtonDefaultSet(String label, String styleName) {
			this.label = label;
			this.styleName = styleName;
		}
		public String getLabel() {
			return label;
		}
		public String getStyleName() {
			return styleName;
		}
	};
	
	public interface ConfirmDialogListener {
		public void onButtonClick(DialogButton button);
	}
	
	/**
	 * Shows the dialog with the specified message and the default set of buttons.
	 * @param message the message to show.
	 * @param listener the dialog listener.
	 */
	public void center(String message, ConfirmDialogListener listener);

	/**
	 * Shows the dialog with the specified message and the specified set of buttons.
	 * @param message the message to show.
	 * @param listener the dialog listener.
	 * @param buttons the set of button to show.
	 */
	public void center(String message, ConfirmDialogListener listener, DialogButton ... buttons);
	
	public void warning(String message);

}