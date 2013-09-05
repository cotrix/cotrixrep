package org.cotrix.web.importwizard.client.util;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class AlertDialog extends PopupPanel {
	
	protected static final int HEIGHT_WITH_ARROW = 115;
	protected static final int HEIGHT = 100;
	
	private static final Binder binder = GWT.create(Binder.class);
	interface Binder extends UiBinder<Widget, AlertDialog> {}

	int height = 0;
	
	@UiField HTML label;
	@UiField Image arrow;
	
	public AlertDialog(boolean showArrow) {
		setWidget(binder.createAndBindUi(this));
		showArrow(showArrow);
		this.setModal(true);
		//this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.center();
		
		addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();

			}}, ClickEvent.getType());

	}
	
	public void showArrow(boolean showArrow)
	{
		arrow.setVisible(showArrow);
		height = showArrow?HEIGHT_WITH_ARROW:HEIGHT;
	}

	public void setMessage(String message){
		this.label.setHTML(message);
	}


	public void showRelative(final UIObject target) {
		// Set the position of the popup right before it is shown.
		setPopupPositionAndShow(new PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				positionRelative(target, offsetWidth, height);
			}
		});
	}

	/**
	 * Positions the popup, called after the offset width and height of the popup
	 * are known.
	 *
	 * @param relativeObject the ui object to position relative to
	 * @param offsetWidth the drop down's offset width
	 * @param offsetHeight the drop down's offset height
	 */
	private void positionRelative(final UIObject relativeObject, int offsetWidth, int offsetHeight) {
		Log.trace("positionRelative offsetWidth: "+offsetWidth+" offsetHeight: "+offsetHeight);

		// Calculate left position for the popup. The computation for
		// the left position is bidi-sensitive.

		int textBoxOffsetWidth = relativeObject.getOffsetWidth();

		// Compute the difference between the popup's width and the
		// textbox's width
		int offsetWidthDiff = offsetWidth - textBoxOffsetWidth;

		int left;

		if (LocaleInfo.getCurrentLocale().isRTL()) { // RTL case
			Log.trace("RTL");

			int textBoxAbsoluteLeft = relativeObject.getAbsoluteLeft();

			// Right-align the popup. Note that this computation is
			// valid in the case where offsetWidthDiff is negative.
			left = textBoxAbsoluteLeft - offsetWidthDiff;

			// If the suggestion popup is not as wide as the text box, always
			// align to the right edge of the text box. Otherwise, figure out whether
			// to right-align or left-align the popup.
			if (offsetWidthDiff > 0) {

				// Make sure scrolling is taken into account, since
				// box.getAbsoluteLeft() takes scrolling into account.
				int windowRight = Window.getClientWidth() + Window.getScrollLeft();
				int windowLeft = Window.getScrollLeft();

				// Compute the left value for the right edge of the textbox
				int textBoxLeftValForRightEdge = textBoxAbsoluteLeft
						+ textBoxOffsetWidth;

				// Distance from the right edge of the text box to the right edge
				// of the window
				int distanceToWindowRight = windowRight - textBoxLeftValForRightEdge;

				// Distance from the right edge of the text box to the left edge of the
				// window
				int distanceFromWindowLeft = textBoxLeftValForRightEdge - windowLeft;

				// If there is not enough space for the overflow of the popup's
				// width to the right of the text box and there IS enough space for the
				// overflow to the right of the text box, then left-align the popup.
				// However, if there is not enough space on either side, stick with
				// right-alignment.
				if (distanceFromWindowLeft < offsetWidth
						&& distanceToWindowRight >= offsetWidthDiff) {
					// Align with the left edge of the text box.
					left = textBoxAbsoluteLeft;
				}
			}
		} else { // LTR case
			Log.trace("LTR");
			// Left-align the popup.
			left = relativeObject.getAbsoluteLeft();

			// If the suggestion popup is not as wide as the text box, always align to
			// the left edge of the text box. Otherwise, figure out whether to
			// left-align or right-align the popup.
			if (offsetWidthDiff > 0) {
				// Make sure scrolling is taken into account, since
				// box.getAbsoluteLeft() takes scrolling into account.
				int windowRight = Window.getClientWidth() + Window.getScrollLeft();
				int windowLeft = Window.getScrollLeft();

				// Distance from the left edge of the text box to the right edge
				// of the window
				int distanceToWindowRight = windowRight - left;

				// Distance from the left edge of the text box to the left edge of the
				// window
				int distanceFromWindowLeft = left - windowLeft;

				// If there is not enough space for the overflow of the popup's
				// width to the right of hte text box, and there IS enough space for the
				// overflow to the left of the text box, then right-align the popup.
				// However, if there is not enough space on either side, then stick with
				// left-alignment.
				if (distanceToWindowRight < offsetWidth
						&& distanceFromWindowLeft >= offsetWidthDiff) {
					// Align with the right edge of the text box.
					left -= offsetWidthDiff;
				}
			}
		}

		// Calculate top position for the popup

		int top = relativeObject.getAbsoluteTop();

		// Make sure scrolling is taken into account, since
		// box.getAbsoluteTop() takes scrolling into account.
		int windowTop = Window.getScrollTop();
		int windowBottom = Window.getScrollTop() + Window.getClientHeight();

		// Distance from the top edge of the window to the top edge of the
		// text box
		int distanceFromWindowTop = top - windowTop;

		// Distance from the bottom edge of the window to the bottom edge of
		// the text box
		int distanceToWindowBottom = windowBottom
				- (top + relativeObject.getOffsetHeight());

		// If there is not enough space for the popup's height below the text
		// box and there IS enough space for the popup's height above the text
		// box, then then position the popup above the text box. However, if there
		// is not enough space on either side, then stick with displaying the
		// popup below the text box.
		if (distanceFromWindowTop >= offsetHeight) {
			top -= offsetHeight;
		} else {
			// Position above the text box
			top += relativeObject.getOffsetHeight();
		}
		setPopupPosition(left, top);
	}

}
