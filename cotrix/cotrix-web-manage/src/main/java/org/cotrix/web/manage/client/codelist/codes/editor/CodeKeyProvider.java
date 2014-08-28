/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor;

import org.cotrix.web.common.shared.codelist.UICode;

import com.google.gwt.view.client.ProvidesKey;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeKeyProvider implements ProvidesKey<UICode> {
	
	public static final CodeKeyProvider INSTANCE = new CodeKeyProvider();

	@Override
	public Object getKey(UICode item) {
		return item.getId();
	}

}
