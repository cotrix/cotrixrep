/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linkdefinition;

import java.util.List;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.shared.CodelistValueTypes;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(LinkDefinitionsCodelistInfoProviderImpl.class)
public interface LinkDefinitionsCodelistInfoProvider {
	
	public void getCodelists(AsyncCallback<List<UICodelist>> callback);
	
	public void getCodelistValueTypes(String codelistid, AsyncCallback<CodelistValueTypes> callback);

}
