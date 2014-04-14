/**
 * 
 */
package com.google.gwt.user.client.ui;

import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionCallback;
import com.google.gwt.user.client.ui.SuggestCell.SuggestCellDisplay;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * <p>
 * The default implementation of {@link CellSuggestionDisplay} displays
 * suggestions in a {@link PopupPanel} beneath the {@link SuggestBox}.
 * </p>
 *
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.gwt-SuggestBoxPopup</dt>
 * <dd>the suggestion popup</dd>
 * <dt>.gwt-SuggestBoxPopup .item</dt>
 * <dd>an unselected suggestion</dd>
 * <dt>.gwt-SuggestBoxPopup .item-selected</dt>
 * <dd>a selected suggestion</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupTopLeft</dt>
 * <dd>the top left cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupTopLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupTopCenter</dt>
 * <dd>the top center cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupTopCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupTopRight</dt>
 * <dd>the top right cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupTopRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleLeft</dt>
 * <dd>the middle left cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleCenter</dt>
 * <dd>the middle center cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleRight</dt>
 * <dd>the middle right cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupBottomLeft</dt>
 * <dd>the bottom left cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupBottomLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupBottomCenter</dt>
 * <dd>the bottom center cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupBottomCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupBottomRight</dt>
 * <dd>the bottom right cell</dd>
 * <dt>.gwt-SuggestBoxPopup .suggestPopupBottomRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * </dl>
 *
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultSuggestCellDisplay extends SuggestCellDisplay implements HasAnimation {

	private final SuggestionMenu suggestionMenu;
	private final PopupPanel suggestionPopup;

	/**
	 * We need to keep track of the last {@link SuggestBox} because it acts as
	 * an autoHide partner for the {@link PopupPanel}. If we use the same
	 * display for multiple {@link SuggestBox}, we need to switch the autoHide
	 * partner.
	 */
	private SuggestCell lastSuggestBox = null;

	/**
	 * Sub-classes making use of {@link decorateSuggestionList} to add
	 * elements to the suggestion popup _may_ want those elements to show even
	 * when there are 0 suggestions. An example would be showing a "No
	 * matches" message.
	 */
	private boolean hideWhenEmpty = true;

	/**
	 * Construct a new {@link DefaultSuggestCellDisplay}.
	 */
	public DefaultSuggestCellDisplay() {
		suggestionMenu = new SuggestionMenu(true);
		suggestionPopup = createPopup();
		suggestionPopup.setWidget(decorateSuggestionList(suggestionMenu));
	}

	@Override
	public void hideSuggestions() {
		suggestionPopup.hide();
	}

	public boolean isAnimationEnabled() {
		return suggestionPopup.isAnimationEnabled();
	}

	/**
	 * Check whether or not the suggestion list is hidden when there are no
	 * suggestions to display.
	 *
	 * @return true if hidden when empty, false if not
	 */
	public boolean isSuggestionListHiddenWhenEmpty() {
		return hideWhenEmpty;
	}

	/**
	 * Check whether or not the list of suggestions is being shown.
	 *
	 * @return true if the suggestions are visible, false if not
	 */
	public boolean isSuggestionListShowing() {
		return suggestionPopup.isShowing();
	}

	public void setAnimationEnabled(boolean enable) {
		suggestionPopup.setAnimationEnabled(enable);
	}

	/**
	 * Sets the style name of the suggestion popup.
	 *
	 * @param style the new primary style name
	 * @see UIObject#setStyleName(String)
	 */
	public void setPopupStyleName(String style) {
		suggestionPopup.setStyleName(style);
	}

	/**
	 * Set whether or not the suggestion list should be hidden when there are
	 * no suggestions to display. Defaults to true.
	 *
	 * @param hideWhenEmpty true to hide when empty, false not to
	 */
	public void setSuggestionListHiddenWhenEmpty(boolean hideWhenEmpty) {
		this.hideWhenEmpty = hideWhenEmpty;
	}

	/**
	 * Create the PopupPanel that will hold the list of suggestions.
	 *
	 * @return the popup panel
	 */
	protected PopupPanel createPopup() {
		PopupPanel p = new DecoratedPopupPanel(true, false, "suggestPopup");
		p.setStyleName("gwt-SuggestBoxPopup");
		p.setPreviewingAllNativeEvents(true);
		p.setAnimationType(AnimationType.ROLL_DOWN);
		return p;
	}

	/**
	 * Wrap the list of suggestions before adding it to the popup. You can
	 * override this method if you want to wrap the suggestion list in a
	 * decorator.
	 *
	 * @param suggestionList the widget that contains the list of suggestions
	 * @return the suggestList, optionally inside of a wrapper
	 */
	protected Widget decorateSuggestionList(Widget suggestionList) {
		return suggestionList;
	}

	@Override
	public Suggestion getCurrentSelection() {
		if (!isSuggestionListShowing()) {
			return null;
		}
		MenuItem item = suggestionMenu.getSelectedItem();
		return item == null ? null : ((SuggestionMenuItem) item).getSuggestion();
	}

	/**
	 * Get the {@link PopupPanel} used to display suggestions.
	 *
	 * @return the popup panel
	 */
	protected PopupPanel getPopupPanel() {
		return suggestionPopup;
	}

	@Override
	public void moveSelectionDown() {
		// Make sure that the menu is actually showing. These keystrokes
		// are only relevant when choosing a suggestion.
		if (isSuggestionListShowing()) {
			// If nothing is selected, getSelectedItemIndex will return -1 and we
			// will select index 0 (the first item) by default.
			suggestionMenu.selectItem(suggestionMenu.getSelectedItemIndex() + 1);
		}
	}

	@Override
	public void moveSelectionUp() {
		// Make sure that the menu is actually showing. These keystrokes
		// are only relevant when choosing a suggestion.
		if (isSuggestionListShowing()) {
			// if nothing is selected, then we should select the last suggestion by
			// default. This is because, in some cases, the suggestions menu will
			// appear above the text box rather than below it (for example, if the
			// text box is at the bottom of the window and the suggestions will not
			// fit below the text box). In this case, users would expect to be able
			// to use the up arrow to navigate to the suggestions.
			if (suggestionMenu.getSelectedItemIndex() == -1) {
				suggestionMenu.selectItem(suggestionMenu.getNumItems() - 1);
			} else {
				suggestionMenu.selectItem(suggestionMenu.getSelectedItemIndex() - 1);
			}
		}
	}

	@Override
	public void showSuggestions(final SuggestCell suggestBox,
			Collection<? extends Suggestion> suggestions,
			boolean isDisplayStringHTML, boolean isAutoSelectEnabled,
			final SuggestionCallback callback) {
		// Hide the popup if there are no suggestions to display.
		boolean anySuggestions = (suggestions != null && suggestions.size() > 0);
		if (!anySuggestions && hideWhenEmpty) {
			hideSuggestions();
			return;
		}

		// Hide the popup before we manipulate the menu within it. If we do not
		// do this, some browsers will redraw the popup as items are removed
		// and added to the menu.
		if (suggestionPopup.isAttached()) {
			suggestionPopup.hide();
		}

		suggestionMenu.clearItems();

		for (final Suggestion curSuggestion : suggestions) {
			final SuggestionMenuItem menuItem = new SuggestionMenuItem(
					curSuggestion, isDisplayStringHTML);
			menuItem.setScheduledCommand(new ScheduledCommand() {
				public void execute() {
					callback.onSuggestionSelected(curSuggestion);
				}
			});

			suggestionMenu.addItem(menuItem);
		}

		if (isAutoSelectEnabled && anySuggestions) {
			// Select the first item in the suggestion menu.
			suggestionMenu.selectItem(0);
		}

		// Link the popup autoHide to the TextBox.
		if (lastSuggestBox != suggestBox) {
			// If the suggest box has changed, free the old one first.
			if (lastSuggestBox != null) {
				suggestionPopup.removeAutoHidePartner(lastSuggestBox.getElement());
			}
			lastSuggestBox = suggestBox;
			suggestionPopup.addAutoHidePartner(suggestBox.getElement());
		}

		// Show the popup under the TextBox.
		System.out.println("SUGGESTION POPUP SUGGESTION POPUP SUGGESTION POPUP SUGGESTION POPUP SUGGESTION POPUP SUGGESTION POPUP");
		suggestionPopup.center();
		//suggestionPopup.show();
		/*suggestionPopup.showRelativeTo(positionRelativeTo != null
				? positionRelativeTo : suggestBox);*/
		suggestionPopup.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				popupPosition(suggestBox.getElement(), offsetWidth, offsetHeight);					
			}
		});
	}

	private void popupPosition(final Element relativeObject, int offsetWidth,
			int offsetHeight) {
		// Calculate left position for the popup. The computation for
		// the left position is bidi-sensitive.

		int textBoxOffsetWidth = relativeObject.getOffsetWidth();

		// Compute the difference between the popup's width and the
		// textbox's width
		int offsetWidthDiff = offsetWidth - textBoxOffsetWidth;

		int left;

		if (LocaleInfo.getCurrentLocale().isRTL()) { // RTL case

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
		if (distanceToWindowBottom < offsetHeight
				&& distanceFromWindowTop >= offsetHeight) {
			top -= offsetHeight;
		} else {
			// Position above the text box
			top += relativeObject.getOffsetHeight();
		}
		suggestionPopup.setPopupPosition(left, top);
	}



	/**
	 * The SuggestionMenu class is used for the display and selection of
	 * suggestions in the SuggestBox widget. SuggestionMenu differs from MenuBar
	 * in that it always has a vertical orientation, and it has no submenus. It
	 * also allows for programmatic selection of items in the menu, and
	 * programmatically performing the action associated with the selected item.
	 * In the MenuBar class, items cannot be selected programatically - they can
	 * only be selected when the user places the mouse over a particlar item.
	 * Additional methods in SuggestionMenu provide information about the number
	 * of items in the menu, and the index of the currently selected item.
	 */
	private static class SuggestionMenu extends MenuBar {

		public SuggestionMenu(boolean vertical) {
			super(vertical);
			// Make sure that CSS styles specified for the default Menu classes
			// do not affect this menu
			setStyleName("");
			setFocusOnHoverEnabled(false);
		}

		public int getNumItems() {
			return getItems().size();
		}

		/**
		 * Returns the index of the menu item that is currently selected.
		 *
		 * @return returns the selected item
		 */
		public int getSelectedItemIndex() {
			// The index of the currently selected item can only be
			// obtained if the menu is showing.
			MenuItem selectedItem = getSelectedItem();
			if (selectedItem != null) {
				return getItems().indexOf(selectedItem);
			}
			return -1;
		}

		/**
		 * Selects the item at the specified index in the menu. Selecting the item
		 * does not perform the item's associated action; it only changes the style
		 * of the item and updates the value of SuggestionMenu.selectedItem.
		 *
		 * @param index index
		 */
		public void selectItem(int index) {
			List<MenuItem> items = getItems();
			if (index > -1 && index < items.size()) {
				itemOver(items.get(index), false);
			}
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void onBrowserEvent(Event event) {
			System.out.println("MenuBar onBrowserEvent target: "+DOM.eventGetTarget(event)+" type: "+DOM.eventGetType(event));
			super.onBrowserEvent(event);
		}
		
		
	}

	/**
	 * Class for menu items in a SuggestionMenu. A SuggestionMenuItem differs from
	 * a MenuItem in that each item is backed by a Suggestion object. The text of
	 * each menu item is derived from the display string of a Suggestion object,
	 * and each item stores a reference to its Suggestion object.
	 */
	private static class SuggestionMenuItem extends MenuItem {

		private static final String STYLENAME_DEFAULT = "item";

		private Suggestion suggestion;

		public SuggestionMenuItem(Suggestion suggestion, boolean asHTML) {
			super(suggestion.getDisplayString(), asHTML);
			// Each suggestion should be placed in a single row in the suggestion
			// menu. If the window is resized and the suggestion cannot fit on a
			// single row, it should be clipped (instead of wrapping around and
			// taking up a second row).
			DOM.setStyleAttribute(getElement(), "whiteSpace", "nowrap");
			setStyleName(STYLENAME_DEFAULT);
			setSuggestion(suggestion);
		}

		public Suggestion getSuggestion() {
			return suggestion;
		}

		public void setSuggestion(Suggestion suggestion) {
			this.suggestion = suggestion;
		}
	}
}

