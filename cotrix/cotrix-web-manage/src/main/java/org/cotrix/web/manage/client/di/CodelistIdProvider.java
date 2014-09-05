/**
 * 
 */
package org.cotrix.web.manage.client.di;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistIdProvider implements Provider<String> {

	protected String codelistId;

	public void setCodelistId(String codelistId) {
		this.codelistId = codelistId;
	}

	@Override
	public String get() {
		return codelistId;
	}

}
