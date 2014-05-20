package com.google.gwt.user.client.ui;

import static com.google.gwt.dom.client.BrowserEvents.*;

import java.util.Collection;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.SuggestTextCell.SuggestTextCellDisplay;


/**
 * An editable text cell. Double click to edit, escape to cancel, return to commit.
 * The code is derived from {@link EditTextCell}.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SuggestCell<T extends Suggestion> extends AbstractEditableCell<T, SuggestCell.ViewData> {

	interface Template extends SafeHtmlTemplates {
		@Template("<input type=\"text\" value=\"{0}\" tabindex=\"-1\" class=\"{1}\"></input>")
		SafeHtml input(String value, String style);
	}
	
	/**
	 * Used to display suggestions to the user.
	 */
	public abstract static class SuggestCellDisplay {

		/**
		 * Get the currently selected {@link Suggestion} in the display.
		 * @return the current suggestion, or null if none selected
		 */
		public abstract Suggestion getCurrentSelection();

		/**
		 * Hide the list of suggestions from view.
		 */
		public abstract void hideSuggestions();

		/**
		 * Highlight the suggestion directly below the current selection in the list.
		 */
		public abstract void moveSelectionDown();

		/**
		 * Highlight the suggestion directly above the current selection in the list.
		 */
		public abstract void moveSelectionUp();

		/**
		 * Accepts information about whether there were more suggestions matching
		 * than were provided to {@link #showSuggestions}.
		 * @param hasMoreSuggestions true if more matches were available
		 * @param numMoreSuggestions number of more matches available. If the
		 *     specific number is unknown, 0 will be passed.
		 */
		public void setMoreSuggestions(boolean hasMoreSuggestions,
				int numMoreSuggestions) {
			// Subclasses may optionally implement.
		}

		/**
		 * Update the list of visible suggestions.
		 * Use care when using isDisplayStringHtml; it is an easy way to expose
		 * script-based security problems.
		 * @param suggestBox the suggest box where the suggestions originated
		 * @param suggestions the suggestions to show
		 * @param isDisplayStringHTML should the suggestions be displayed as HTML
		 * @param isAutoSelectEnabled if true, the first item should be selected automatically
		 * @param callback the callback used when the user makes a suggestion
		 */
		public abstract void showSuggestions(SuggestCell suggestBox,
				Collection<? extends Suggestion> suggestions,
				boolean isDisplayStringHTML, boolean isAutoSelectEnabled,
				SuggestionCallback callback);
	}

	/**
	 * The view data object used by this cell. We need to store both the text
	 * and the state because this cell is rendered differently in edit mode. If
	 * we did not store the edit state, refreshing the cell with view data would
	 * always put us in to edit state, rendering a text box instead of the new
	 * text string.
	 */
	static class ViewData {

		private boolean isEditing;

		/**
		 * If true, this is not the first edit.
		 */
		private boolean isEditingAgain;

		/**
		 * Keep track of the original value at the start of the edit, which
		 * might be the edited value from the previous edit and NOT the actual
		 * value.
		 */
		private String original;

		private String text;

		/**
		 * Construct a new ViewData in editing mode.
		 * @param text the text to edit
		 */
		public ViewData(String text) {
			this.original = text;
			this.text = text;
			this.isEditing = true;
			this.isEditingAgain = false;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) {
				return false;
			}
			ViewData vd = (ViewData) o;
			return equalsOrBothNull(original, vd.original)
					&& equalsOrBothNull(text, vd.text)
					&& isEditing == vd.isEditing
					&& isEditingAgain == vd.isEditingAgain;
		}

		public String getOriginal() {
			return original;
		}

		public String getText() {
			return text;
		}

		@Override
		public int hashCode() {
			return original.hashCode() + text.hashCode()
					+ Boolean.valueOf(isEditing).hashCode() * 29
					+ Boolean.valueOf(isEditingAgain).hashCode();
		}

		public boolean isEditing() {
			return isEditing;
		}

		public boolean isEditingAgain() {
			return isEditingAgain;
		}

		public void setEditing(boolean isEditing) {
			boolean wasEditing = this.isEditing;
			this.isEditing = isEditing;

			// This is a subsequent edit, so start from where we left off.
			if (!wasEditing && isEditing) {
				isEditingAgain = true;
				original = text;
			}
		}

		public void setText(String text) {
			this.text = text;
		}

		private boolean equalsOrBothNull(Object o1, Object o2) {
			return (o1 == null) ? o2 == null : o1.equals(o2);
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ViewData [isEditing=");
			builder.append(isEditing);
			builder.append(", isEditingAgain=");
			builder.append(isEditingAgain);
			builder.append(", original=");
			builder.append(original);
			builder.append(", text=");
			builder.append(text);
			builder.append("]");
			return builder.toString();
		}
	}
	
	private Element element;
	private int limit = 20;
	private boolean selectsFirstItem = true;
	private SuggestOracle oracle;
	private String currentText;
	private final SuggestCellDisplay display;
	private final Callback callback = new Callback() {
		public void onSuggestionsReady(Request request, Response response) {
			display.setMoreSuggestions(response.hasMoreSuggestions(),
					response.getMoreSuggestionsCount());
			display.showSuggestions(SuggestCell.this, response.getSuggestions(),
					oracle.isDisplayStringHTML(), isAutoSelectEnabled(),
					suggestionCallback);
		}
	};
	
	private final SuggestionCallback suggestionCallback = new SuggestionCallback() {
		public void onSuggestionSelected(Suggestion suggestion) {
			updateSuggestionSelection();
			
			String text = suggestion.getReplacementString();
			lastViewData.setText(text);
			lastViewData.setEditing(false);
			
			T item = (T) suggestion;
			
			setValue(lastContext, lastParent, item);
			if (lastValueUpdater != null) {
				lastValueUpdater.update(item);
			}
		}
	};

	private static Template template;
	private final SafeHtmlRenderer<String> renderer;
	
	protected final String startEditEventName;

	protected boolean readOnly;
	protected String editorStyle;
	
	private InputElement lastInputElement;
	private ViewData lastViewData;
	private Context lastContext;
	private Element lastParent;
	private ValueUpdater<T> lastValueUpdater;
	
	/**
	 * Construct a new EditTextCell that will use a {@link SimpleSafeHtmlRenderer}.
	 */
	public SuggestCell(String editorStyle, SuggestOracle oracle) {
		this(DBLCLICK, editorStyle, SimpleSafeHtmlRenderer.getInstance(), new DefaultSuggestCellDisplay(), oracle);
	}

	/**
	 * Construct a new EditTextCell that will use a given
	 * {@link SafeHtmlRenderer} to render the value when not in edit mode.
	 * 
	 * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
	 */
	public SuggestCell(String startEditEventName, String editorStyle, SafeHtmlRenderer<String> renderer, SuggestCellDisplay display, SuggestOracle oracle) {
		super(startEditEventName, KEYUP, KEYDOWN, BLUR);
		
		if (template == null) {
			template = GWT.create(Template.class);
		}
		
		if (renderer == null) {
			throw new IllegalArgumentException("The renderer is null");
		}
		
		this.startEditEventName = startEditEventName;
		
		this.renderer = renderer;
		this.readOnly = true;
		this.editorStyle = editorStyle;
		
		this.display = display;
		this.oracle = oracle;
	}
	
	public Element getElement() {
		return lastInputElement;
	}

	/**
	 * @return the editable
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the editable to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/**
	 * Gets the limit for the number of suggestions that should be displayed for this box. 
	 * It is up to the current {@link SuggestOracle} to enforce this limit.
	 * @return the limit for the number of suggestions
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Get the {@link SuggestTextCellDisplay} used to display suggestions.
	 * @return the {@link SuggestTextCellDisplay}
	 */
	public SuggestCellDisplay getSuggestionDisplay() {
		return display;
	}

	/**
	 * Gets the suggest box's {@link com.google.gwt.user.client.ui.SuggestOracle}.
	 * @return the {@link SuggestOracle}
	 */
	public SuggestOracle getSuggestOracle() {
		return oracle;
	}

	/**
	 * Returns whether or not the first suggestion will be automatically selected. This behavior is on by default.
	 * @return true if the first suggestion will be automatically selected
	 */
	public boolean isAutoSelectEnabled() {
		return selectsFirstItem;
	}

	/**
	 * Turns on or off the behavior that automatically selects the first suggested item. This behavior is on by default.
	 * @param selectsFirstItem Whether or not to automatically select the first suggestion
	 */
	public void setAutoSelectEnabled(boolean selectsFirstItem) {
		this.selectsFirstItem = selectsFirstItem;
	}

	/**
	 * Sets the limit to the number of suggestions the oracle should provide. It is up to the oracle to enforce this limit.
	 * @param limit the limit to the number of suggestions provided
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean isEditing(Context context, Element parent, T value) {
		ViewData viewData = getViewData(context.getKey());
		return viewData == null ? false : viewData.isEditing();
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, T value,
			NativeEvent event, ValueUpdater<T> valueUpdater) {
		Object key = context.getKey();
		ViewData viewData = getViewData(key);
		if (viewData != null && viewData.isEditing()) {
			// Handle the edit event.
			editEvent(context, parent, value, viewData, event, valueUpdater);
		} else {

			if (!readOnly) {
				String type = event.getType();
				int keyCode = event.getKeyCode();
				boolean enterPressed = KEYUP.equals(type)
						&& keyCode == KeyCodes.KEY_ENTER;
				if (startEditEventName.equals(type) || enterPressed) {
					System.out.println("DBLClick or ENTER ");
					EventTarget eventTarget = event.getEventTarget();
					if (Element.is(eventTarget)) {
						Element target = Element.as(eventTarget);
						System.out.println("-> target parent"+target.getParentElement().getTagName());
						System.out.println("-> target parent"+target);
					}
					
					// Go into edit mode.
					if (viewData == null) {
						viewData = new ViewData(value.getDisplayString());
						setViewData(key, viewData);
					} else {
						viewData.setEditing(true);
					}
					edit(context, parent, value, viewData, valueUpdater);
				}
			}
		}
	}

	@Override
	public void render(Context context, T value, SafeHtmlBuilder sb) {
		// Get the view data.
		Object key = context.getKey();
		ViewData viewData = getViewData(key);
		if (viewData != null && !viewData.isEditing() && value != null
				&& value.equals(viewData.getText())) {
			clearViewData(key);
			viewData = null;
		}

		String toRender = value == null?"":value.getDisplayString();
		if (viewData != null) {
			String text = viewData.getText();
			if (viewData.isEditing()) {
				/*
				 * Do not use the renderer in edit mode because the value of a
				 * text input element is always treated as text. SafeHtml isn't
				 * valid in the context of the value attribute.
				 */
				sb.append(template.input(text, editorStyle));
				return;
			} else {
				// The user pressed enter, but view data still exists.
				toRender = text;
			}
		}

		if (toRender != null && toRender.trim().length() > 0) {
			sb.append(renderer.render(toRender));
		} else {
			/*
			 * Render a blank space to force the rendered element to have a
			 * height. Otherwise it is not clickable.
			 */
			sb.appendHtmlConstant("\u00A0");
		}
	}

	@Override
	public boolean resetFocus(Context context, Element parent, T value) {
		if (isEditing(context, parent, value)) {
			getInputElement(parent).focus();
			return true;
		}
		return false;
	}

	/**
	 * Convert the cell to edit mode.
	 * @param context the {@link Context} of the cell
	 * @param parent the parent element
	 * @param value the current value
	 */
	private void edit(Context context, Element parent, T value, ViewData viewData, ValueUpdater<T> valueUpdater) {
		setValue(context, parent, value);
		InputElement input = getInputElement(parent);
		input.focus();
		input.select();
		
		this.lastInputElement = input;
		this.lastViewData = viewData;
		this.lastContext = context;
		this.lastParent= parent;
		this.lastValueUpdater = valueUpdater;
	}

	/**
	 * Convert the cell to non-edit mode.
	 * @param context the context of the cell
	 * @param parent the parent Element
	 * @param value the value associated with the cell
	 */
	private void cancel(Context context, Element parent, T value) {
		clearInput(getInputElement(parent));
		setValue(context, parent, value);
	}

	/**
	 * Clear selected from the input element. Both Firefox and IE fire spurious
	 * onblur events after the input is removed from the DOM if selection is not
	 * cleared.
	 * @param input the input element
	 */
	private native void clearInput(Element input) /*-{
		if (input.selectionEnd)
			input.selectionEnd = input.selectionStart;
		else if ($doc.selection)
			$doc.selection.clear();
	}-*/;

	/**
	 * Commit the current value.
	 * @param context the context of the cell
	 * @param parent the parent Element
	 * @param viewData the {@link ViewData} object
	 * @param valueUpdater the {@link ValueUpdater}
	 */
	private void commit(Context context, Element parent, ViewData viewData, ValueUpdater<T> valueUpdater, Suggestion suggestion) {
		
		System.out.println("commit viewData: "+viewData);
		String value = updateViewData(parent, viewData, false);
		System.out.println("value: "+value);
		clearInput(getInputElement(parent));
		T item = (T) suggestion;
		setValue(context, parent, item);
		if (valueUpdater != null) {
			valueUpdater.update(item);
		}
	}

	private void editEvent(Context context, Element parent, T value, ViewData viewData, NativeEvent event, ValueUpdater<T> valueUpdater) {
		String type = event.getType();
		boolean keyUp = KEYUP.equals(type);
		boolean keyDown = KEYDOWN.equals(type);
		if (keyUp || keyDown) {
			
			int keyCode = event.getKeyCode();
			if (keyUp && keyCode == KeyCodes.KEY_ENTER) {
				System.out.println("Enter");
				
				Suggestion suggestion = updateSuggestionSelection();
				
				// Commit the change.
				commit(context, parent, viewData, valueUpdater, suggestion);
			} else if (keyUp && keyCode == KeyCodes.KEY_ESCAPE) {
				System.out.println("ESCAPE");
				
				// Cancel edit mode.
				display.hideSuggestions();
				
				String originalText = viewData.getOriginal();
				if (viewData.isEditingAgain()) {
					viewData.setText(originalText);
					viewData.setEditing(false);
				} else {
					setViewData(context.getKey(), null);
				}
				cancel(context, parent, value);
			}  else if (keyDown && keyCode == KeyCodes.KEY_DOWN) {
				display.moveSelectionDown();
			} else if (keyDown && keyCode == KeyCodes.KEY_UP) {
				display.moveSelectionUp();
			} else {

				// Update the text in the view data on each key.
				String text = updateViewData(parent, viewData, true);
				if (keyUp) refreshSuggestions(text);
				
			}
		} else if (BLUR.equals(type)) {
			// Commit the change. Ensure that we are blurring the input element
			// and
			// not the parent element itself.
			EventTarget eventTarget = event.getEventTarget();
			if (Element.is(eventTarget)) {
				Element target = Element.as(eventTarget);
				if ("input".equals(target.getTagName().toLowerCase())) {
					commit(context, parent, viewData, valueUpdater, null);
				}
			}
		}
	}

	private void showSuggestions(String query) {
		System.out.println("showSuggestions query: "+query);
		if (query.length() == 0) {
			oracle.requestDefaultSuggestions(new Request(null, limit), callback);
		} else {
			oracle.requestSuggestions(new Request(query, limit), callback);
		}
	}

	/**
	 * Get the input element in edit mode.
	 */
	private InputElement getInputElement(Element parent) {
		return parent.getFirstChild().<InputElement> cast();
	}

	/**
	 * Update the view data based on the current value.
	 * @param parent the parent element
	 * @param viewData the {@link ViewData} object to update
	 * @param isEditing true if in edit mode
	 * @return the new value
	 */
	private String updateViewData(Element parent, ViewData viewData, boolean isEditing) {
		InputElement input = (InputElement) parent.getFirstChild();
		String value = input.getValue();
		viewData.setText(value);
		viewData.setEditing(isEditing);
		return value;
	}
	
	private void refreshSuggestions(String text) {
		// Get the raw text.

		if (text.equals(currentText)) {
			return;
		} else {
			currentText = text;
		}
		showSuggestions(text);
	}

	/**
	 * Set the new suggestion in the text box.
	 * @param curSuggestion the new suggestion
	 */
	private Suggestion updateSuggestionSelection() {
		Suggestion suggestion = display.getCurrentSelection();
		display.hideSuggestions();
		if (suggestion == null) return null;
		
		currentText = suggestion.getReplacementString();
		lastInputElement.setValue(currentText);
		return suggestion;
	}
}
