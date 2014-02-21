/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.util.Constants;
import org.cotrix.web.share.shared.codelist.UICode;
import org.cotrix.web.share.shared.codelist.UIQName;

import com.google.gwt.dom.client.Document;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeFactory {
	
	@Inject
	protected static Constants constants;
	
	public static UICode createCode()
	{

		UIQName name = new UIQName(constants.getDefaultNamespace(), constants.getDefaultCodeName());
		UICode code = new UICode(Document.get().createUniqueId(), name);
		return code;
	}

}
