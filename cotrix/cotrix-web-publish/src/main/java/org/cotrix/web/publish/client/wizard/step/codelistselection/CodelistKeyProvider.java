/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step.codelistselection;

import org.cotrix.web.publish.shared.Codelist;

import com.google.gwt.view.client.ProvidesKey;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistKeyProvider implements ProvidesKey<Codelist> {
	
	public static final CodelistKeyProvider INSTANCE = new CodelistKeyProvider();

	@Override
	public Object getKey(Codelist item) {
		return item==null?null:item.getId();
	}

}
