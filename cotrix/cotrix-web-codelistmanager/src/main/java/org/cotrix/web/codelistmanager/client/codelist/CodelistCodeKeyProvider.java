/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.shared.UICode;

import com.google.gwt.view.client.ProvidesKey;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistCodeKeyProvider implements ProvidesKey<UICode> {
	
	public static final CodelistCodeKeyProvider INSTANCE = new CodelistCodeKeyProvider();

	@Override
	public Object getKey(UICode item) {
		return item.getId();
	}

}
