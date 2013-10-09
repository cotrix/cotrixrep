/**
 * 
 */
package org.cotrix.web.codelistmanager.client.di;

import com.google.inject.Provider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistIdProvider implements Provider<String> {

	protected String codelistId;

	/**
	 * @param codelistId the codelistId to set
	 */
	public void setCodelistId(String codelistId) {
		this.codelistId = codelistId;
	}

	@Override
	public String get() {
		return codelistId;
	}

}
