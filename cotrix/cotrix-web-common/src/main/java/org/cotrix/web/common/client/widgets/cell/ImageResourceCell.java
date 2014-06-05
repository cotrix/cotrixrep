/**
 * 
 */
package org.cotrix.web.common.client.widgets.cell;

import static com.google.gwt.dom.client.BrowserEvents.*;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * An {@link AbstractCell} used to render an {@link ImageResource}.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImageResourceCell<T> extends AbstractCell<T> {
	private SafeHtmlRenderer<T> renderer;

	/**
	 * Construct a new ImageResourceCell.
	 */
	public ImageResourceCell(SafeHtmlRenderer<T> renderer) {
		super(CLICK, KEYDOWN);
		this.renderer = renderer;
	}

	/** 
	 * {@inheritDoc}
	 */
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
	public void render(Context context, T value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(renderer.render(value));
		}
	}

}
