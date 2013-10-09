/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.shared.UICodelistRow;

import com.google.gwt.view.client.ProvidesKey;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistRowKeyProvider implements ProvidesKey<UICodelistRow> {
	
	public static final CodelistRowKeyProvider INSTANCE = new CodelistRowKeyProvider();

	@Override
	public Object getKey(UICodelistRow item) {
		return item.getId();
	}

}
