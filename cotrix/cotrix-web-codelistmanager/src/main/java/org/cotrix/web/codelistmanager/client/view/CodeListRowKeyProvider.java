/**
 * 
 */
package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.codelistmanager.shared.UICodeListRow;

import com.google.gwt.view.client.ProvidesKey;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListRowKeyProvider implements ProvidesKey<UICodeListRow> {
	
	public static final CodeListRowKeyProvider INSTANCE = new CodeListRowKeyProvider();

	@Override
	public Object getKey(UICodeListRow item) {
		return item.getId();
	}

}
