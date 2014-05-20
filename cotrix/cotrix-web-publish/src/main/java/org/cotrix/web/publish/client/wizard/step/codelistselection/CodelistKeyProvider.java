/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step.codelistselection;

import org.cotrix.web.common.shared.codelist.UICodelist;

import com.google.gwt.view.client.ProvidesKey;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistKeyProvider implements ProvidesKey<UICodelist> {
	
	public static final CodelistKeyProvider INSTANCE = new CodelistKeyProvider();

	@Override
	public Object getKey(UICodelist item) {
		return item==null?null:item.getId();
	}

}
