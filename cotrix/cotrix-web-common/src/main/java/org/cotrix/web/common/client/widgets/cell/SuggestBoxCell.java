/**
 * 
 */
package org.cotrix.web.common.client.widgets.cell;

import static com.google.gwt.dom.client.BrowserEvents.*;

import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.client.ui.DefaultSuggestTextCellDisplay;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestTextCell;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SuggestBoxCell extends SuggestTextCell implements EditableCell {

	public SuggestBoxCell(String editorStyle, SafeHtmlRenderer<String> render, SuggestOracle oracle) {
		super(DBLCLICK, editorStyle, render, new DefaultSuggestTextCellDisplay(), oracle);
	}
}
