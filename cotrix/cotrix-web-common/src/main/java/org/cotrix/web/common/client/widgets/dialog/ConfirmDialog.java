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
		int getWidth();
	}
	
	public class SimpleDialogButton implements DialogButton {
		private String label;
		private String styleName;
		private int width;

		public SimpleDialogButton(String label, String styleName, int width) {
			this.label = label;
			this.styleName = styleName;
			this.width = width;
		}
		
		public String getLabel() {
			return label;
		}
		
		public String getStyleName() {
			return styleName;
		}
		
		public int getWidth() {
			return width;
		}
	}
	
	public enum DialogButtonDefaultSet implements DialogButton {
		CONTINUE("Continue", CommonResources.INSTANCE.css().blueButton(), 98), 
		CANCEL("Cancel", CommonResources.INSTANCE.css().grayButton(), 98);
		
		private String label;
		private String styleName;
		private int width;

		private DialogButtonDefaultSet(String label, String styleName, int width) {
			this.label = label;
			this.styleName = styleName;
			this.width = width;
		}
		public String getLabel() {
			return label;
		}
		public String getStyleName() {
			return styleName;
		}
		public int getWidth() {
			return width;
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