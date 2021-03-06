/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import java.util.List;

import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UILinkDefinitionInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(LinksCodelistInfoProviderImpl.class)
public interface LinksCodelistInfoProvider {
	
	public void getCodelistLinkDefinitions(AsyncCallback<List<UILinkDefinitionInfo>> callback);
	
	public void getCodelistCodes(String linkTypeId, AsyncCallback<List<UICodeInfo>> callback);

}
