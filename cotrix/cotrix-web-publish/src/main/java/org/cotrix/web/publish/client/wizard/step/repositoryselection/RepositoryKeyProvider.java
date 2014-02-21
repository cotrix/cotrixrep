/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step.repositoryselection;

import org.cotrix.web.publish.shared.UIRepository;

import com.google.gwt.view.client.ProvidesKey;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryKeyProvider implements ProvidesKey<UIRepository> {
	
	public static final RepositoryKeyProvider INSTANCE = new RepositoryKeyProvider();

	@Override
	public Object getKey(UIRepository item) {
		return item==null?null:item.getId();
	}

}
