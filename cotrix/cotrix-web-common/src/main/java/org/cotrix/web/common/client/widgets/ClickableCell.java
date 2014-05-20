/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import static com.google.gwt.dom.client.BrowserEvents.*;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ClickableCell<T>  extends AbstractSafeHtmlCell<T> {

	/**
	 * Construct a new ClickableCell that will use a given
	 * {@link SafeHtmlRenderer}.
	 * 
	 * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
	 */
	public ClickableCell(SafeHtmlRenderer<T> renderer) {
		super(renderer, CLICK, KEYDOWN);
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, T value,
			NativeEvent event, ValueUpdater<T> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if (CLICK.equals(event.getType())) {
			onEnterKeyDown(context, parent, value, event, valueUpdater);
		}
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent, T value,
			NativeEvent event, ValueUpdater<T> valueUpdater) {
		if (valueUpdater != null) {
			valueUpdater.update(value);
		}
	}

	@Override
	protected void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}
}

