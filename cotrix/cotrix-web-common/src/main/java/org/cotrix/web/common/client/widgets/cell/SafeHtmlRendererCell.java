/**
 * 
 */
package org.cotrix.web.common.client.widgets.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SafeHtmlRendererCell<C> extends AbstractCell<C> {
	
	private SafeHtmlRenderer<C> renderer;

	public SafeHtmlRendererCell(SafeHtmlRenderer<C> renderer) {
		super();
		this.renderer = renderer;
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,	C value, SafeHtmlBuilder sb) {
		renderer.render(value, sb);
		
	}

}
