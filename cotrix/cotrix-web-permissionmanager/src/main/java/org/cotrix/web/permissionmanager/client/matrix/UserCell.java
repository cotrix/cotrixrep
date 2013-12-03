/**
 * 
 */
package org.cotrix.web.permissionmanager.client.matrix;

import org.cotrix.web.share.client.resources.CommonResources;
import org.cotrix.web.share.client.widgets.SuggestBoxCell;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserCell extends SuggestBoxCell {
	
	protected boolean renderAsInput = true;
	
	public UserCell(SuggestOracle oracle) {
		super(CommonResources.INSTANCE.css().textBox(), oracle);
	}

	/**
	 * @return the renderAsInput
	 */
	public boolean isRenderAsInput() {
		return renderAsInput;
	}

	/**
	 * @param renderAsInput the renderAsInput to set
	 */
	public void setRenderAsInput(boolean renderAsInput) {
		this.renderAsInput = renderAsInput;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
			Element parent, String value, NativeEvent event,
			ValueUpdater<String> valueUpdater) {
		if (renderAsInput) super.onBrowserEvent(context, parent, value, event, valueUpdater);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			String value, SafeHtmlBuilder sb) {
		if (renderAsInput) super.render(context, value, sb);
		else sb.append(SafeHtmlUtils.fromString(value));
	}
}
