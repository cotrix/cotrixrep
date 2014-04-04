/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.List;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.link.AttributeType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(CodelistInfoProviderImpl.class)
public interface CodelistInfoProvider {
	
	public void getCodelists(AsyncCallback<List<UICodelist>> callback);
	
	public void getAttributes(String codelistid, AsyncCallback<List<AttributeType>> callback);

}
