/**
 * 
 */
package org.cotrix.web.manage.client.di;

import org.cotrix.web.common.shared.codelist.UICodelist;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistProvider implements Provider<UICodelist> {

	protected UICodelist codelist;

	/**
	 * @param codelist the codelist to set
	 */
	public void setCodelist(UICodelist codelist) {
		this.codelist = codelist;
	}

	@Override
	public UICodelist get() {
		return codelist;
	}

}
