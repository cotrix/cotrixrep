/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that displays progress on an arbitrary scale.
 */
public class ProgressBar extends Widget {

	interface ProgressBarCss extends CssResource {
		String shell();
		String bar();
		String text();
	}

	interface ProgressBarResources extends ClientBundle {
		@Source("progressbar.css")
		ProgressBarCss css();
	}

	/**
	 * A formatter used to format the text displayed in the progress bar widget.
	 */
	public abstract static class TextFormatter {
		/**
		 * Generate the text to display in the ProgressBar based on the current
		 * value.
		 * 
		 * Override this method to change the text displayed within the ProgressBar.
		 * 
		 * @param bar the progress bar
		 * @param curProgress the current progress
		 * @return the text to display in the progress bar
		 */
		protected abstract String getText(ProgressBar bar, double curProgress);
	}

	protected ProgressBarResources resources = GWT.create(ProgressBarResources.class);
	protected ProgressBarCss css = resources.css();

	/**
	 * The bar element that displays the progress.
	 */
	private Element barElement;

	/**
	 * The current progress.
	 */
	private double curProgress;

	/**
	 * The maximum progress.
	 */
	private double maxProgress;

	/**
	 * The minimum progress.
	 */
	private double minProgress;

	/**
	 * A boolean that determines if the text is visible.
	 */
	private boolean textVisible = true;

	/**
	 * The element that displays text on the page.
	 */
	private Element textElement;

	/**
	 * The current text formatter.
	 */
	private TextFormatter textFormatter;

	/**
	 * Create a progress bar with default range of 0 to 100.
	 */
	public ProgressBar() {
		this(0.0, 100.0, 0.0);
	}

	/**
	 * Create a progress bar with an initial progress and a default range of 0 to
	 * 100.
	 * 
	 * @param curProgress the current progress
	 */
	public ProgressBar(double curProgress) {
		this(0.0, 100.0, curProgress);
	}

	/**
	 * Create a progress bar within the given range.
	 * 
	 * @param minProgress the minimum progress
	 * @param maxProgress the maximum progress
	 */
	public ProgressBar(double minProgress, double maxProgress) {
		this(minProgress, maxProgress, 0.0);
	}

	/**
	 * Create a progress bar within the given range starting at the specified
	 * progress amount.
	 * 
	 * @param minProgress the minimum progress
	 * @param maxProgress the maximum progress
	 * @param curProgress the current progress
	 */
	public ProgressBar(double minProgress, double maxProgress, double curProgress) {
		this(minProgress, maxProgress, curProgress, null);
	}

	/**
	 * Create a progress bar within the given range starting at the specified
	 * progress amount.
	 * 
	 * @param minProgress the minimum progress
	 * @param maxProgress the maximum progress
	 * @param curProgress the current progress
	 * @param textFormatter the text formatter
	 */
	public ProgressBar(double minProgress, double maxProgress,
			double curProgress, TextFormatter textFormatter) {
		this.minProgress = minProgress;
		this.maxProgress = maxProgress;
		this.curProgress = curProgress;
		setTextFormatter(textFormatter);

		css.ensureInjected();

		// Create the outer shell
		setElement((Element) Document.get().createDivElement().cast());
		getElement().getStyle().setPosition(Position.RELATIVE);
		setStyleName(css.shell());

		// Create the bar element
		barElement = DOM.createDiv();
		getElement().appendChild(barElement);
		barElement.getStyle().setHeight(100, Unit.PCT);
		setBarStyleName(css.bar());

		// Create the text element
		textElement = DOM.createDiv();
		getElement().appendChild(textElement);
		textElement.getStyle().setPosition(Position.ABSOLUTE);
		textElement.getStyle().setTop(0, Unit.PX);

		// Set the current progress
		setProgress(curProgress);
	}

	/**
	 * Get the maximum progress.
	 *
	 * @return the maximum progress
	 */
	public double getMaxProgress() {
		return maxProgress;
	}

	/**
	 * Get the minimum progress.
	 *
	 * @return the minimum progress
	 */
	public double getMinProgress() {
		return minProgress;
	}

	/**
	 * Get the current percent complete, relative to the minimum and maximum
	 * predefinedUsers. The percent will always be between 0.0 - 1.0.
	 *
	 * @return the current percent complete
	 */
	public double getPercent() {
		// If we have no range
		if (maxProgress <= minProgress) {
			return 0.0;
		}

		// Calculate the relative progress
		double percent = (curProgress - minProgress) / (maxProgress - minProgress);
		return Math.max(0.0, Math.min(1.0, percent));
	}

	/**
	 * Get the current progress.
	 *
	 * @return the current progress
	 */
	public double getProgress() {
		return curProgress;
	}

	/**
	 * Get the text formatter.
	 *
	 * @return the text formatter
	 */
	public TextFormatter getTextFormatter() {
		return textFormatter;
	}

	/**
	 * Check whether the text is visible or not.
	 *
	 * @return true if the text is visible
	 */
	public boolean isTextVisible() {
		return textVisible;
	}

	/**
	 * This method is called when the dimensions of the parent element change.
	 * Subclasses should override this method as needed.
	 *
	 * Move the text to the center of the progress bar.
	 *
	 * @param width the new client width of the element
	 * @param height the new client height of the element
	 */
	public void onResize(int width, int height) {
		if (textVisible) {
			int textWidth = textElement.getOffsetWidth();
			int left = (width / 2) - (textWidth / 2);
			textElement.getStyle().setLeft(left, Unit.PX);
		}
	}

	/**
	 * Redraw the progress bar when something changes the layout.
	 */
	public void redraw() {
		if (isAttached()) {
			int width = getElement().getClientWidth();
			int height = getElement().getClientHeight();
			onResize(width, height);
		}
	}

	public void setBarStyleName(String barClassName) {
		barElement.setClassName(barClassName);
	}

	/**
	 * Set the maximum progress. If the minimum progress is more than the current
	 * progress, the current progress is adjusted to be within the new range.
	 *
	 * @param maxProgress the maximum progress
	 */
	public void setMaxProgress(double maxProgress) {
		this.maxProgress = maxProgress;
		curProgress = Math.min(curProgress, maxProgress);
		resetProgress();
	}

	/**
	 * Set the minimum progress. If the minimum progress is more than the current
	 * progress, the current progress is adjusted to be within the new range.
	 *
	 * @param minProgress the minimum progress
	 */
	public void setMinProgress(double minProgress) {
		this.minProgress = minProgress;
		curProgress = Math.max(curProgress, minProgress);
		resetProgress();
	}

	/**
	 * Set the current progress.
	 *
	 * @param curProgress the current progress
	 */
	public void setProgress(double curProgress) {
		this.curProgress = Math.max(minProgress, Math.min(maxProgress, curProgress));

		// Calculate percent complete
		int percent = (int) (100 * getPercent());
		barElement.getStyle().setWidth(percent, Unit.PCT);
		textElement.setInnerHTML(generateText(curProgress));
		updateTextStyle(percent);

		// Realign the text
		redraw();
	}

	/**
	 * Set the text formatter.
	 *
	 * @param textFormatter the text formatter
	 */
	public void setTextFormatter(TextFormatter textFormatter) {
		this.textFormatter = textFormatter;
	}
	/**
	 * Sets whether the text is visible over the bar.
	 *
	 * @param textVisible True to show text, false to hide it
	 */
	public void setTextVisible(boolean textVisible) {
		this.textVisible = textVisible;
		if (this.textVisible) {
			textElement.getStyle().setDisplay(null);
			redraw();
		} else {
			textElement.getStyle().setDisplay(Display.NONE);
		}
	}

	/**
	 * Generate the text to display within the progress bar. Override this
	 * function to change the default progress percent to a more informative
	 * message, such as the number of kilobytes downloaded.
	 *
	 * @param curProgress the current progress
	 * @return the text to display in the progress bar
	 */
	protected String generateText(double curProgress) {
		if (textFormatter != null) {
			return textFormatter.getText(this, curProgress);
		} else {
			return (int) (100 * getPercent()) + "%";
		}
	}


	/**
	 * Reset the progress text based on the current min and max progress range.
	 */
	private void resetProgress() {
		setProgress(getProgress());
	}

	private void updateTextStyle(int percent) {
		textElement.setClassName(css.text());
	}
}