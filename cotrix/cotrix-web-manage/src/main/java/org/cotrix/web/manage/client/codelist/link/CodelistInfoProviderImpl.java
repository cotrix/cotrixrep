/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.List;

import org.cotrix.common.cdi.Current;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.link.AttributeType;
import org.cotrix.web.manage.client.ManageServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistInfoProviderImpl implements CodelistInfoProvider {
	
	@Inject
	protected ManageServiceAsync managerService;
	
	@Inject @Current
	protected UICodelist codelist;

	@Override
	public void getCodelists(final AsyncCallback<List<UICodelist>> callback) {
		managerService.getCodelists(new AsyncCallback<List<UICodelist>>() {
			
			@Override
			public void onSuccess(List<UICodelist> result) {
				result.remove(codelist);
				callback.onSuccess(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getAttributes(String codelistid, AsyncCallback<List<AttributeType>> callback) {
		managerService.getAttributeTypes(codelistid, callback);
	}

}
