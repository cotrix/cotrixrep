/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import java.util.List;

import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UILinkTypeInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(LinksCodelistInfoProviderImpl.class)
public interface LinksCodelistInfoProvider {
	
	public void getCodelistLinkTypes(AsyncCallback<List<UILinkTypeInfo>> callback);
	
	public void getCodelistCodes(String linkTypeId, AsyncCallback<List<UICodeInfo>> callback);

}
