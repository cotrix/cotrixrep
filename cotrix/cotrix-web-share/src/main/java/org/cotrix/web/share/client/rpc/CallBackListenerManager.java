/**
 * 
 */
package org.cotrix.web.share.client.rpc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CallBackListenerManager {
	
	protected List<CallBackListener> interceptors = new ArrayList<CallBackListener>();
	
	public void registerInterceptor(CallBackListener interceptor)
	{
		interceptors.add(interceptor);
	}
	
	public boolean onFailure(Throwable caught)
	{
		for (CallBackListener interceptor:interceptors) {
			boolean propagate = interceptor.onFailure(caught);
			if (!propagate) return false;
		}
		return true;		
	}
	
	public boolean onSuccess(Object result)
	{
		for (CallBackListener interceptor:interceptors) {
			boolean propagate = interceptor.onSuccess(result);
			if (!propagate) return false;
		}
		return true;		
	}
	
	
	public void removeInterceptor(CallBackListener interceptor)
	{
		interceptors.remove(interceptor);
	}

}
