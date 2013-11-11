/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step.repositoryselection;

import org.cotrix.web.share.shared.codelist.UICodelist;

import com.google.gwt.view.client.ProvidesKey;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryKeyProvider implements ProvidesKey<UICodelist> {
	
	public static final RepositoryKeyProvider INSTANCE = new RepositoryKeyProvider();

	@Override
	public Object getKey(UICodelist item) {
		return item==null?null:item.getId();
	}

}
