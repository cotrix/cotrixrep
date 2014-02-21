/**
 * 
 */
package org.cotrix.web.common.gen;

import org.cotrix.web.common.client.rpc.CotrixRemoteServiceProxy;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.rebind.rpc.ProxyCreator;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixProxyCreator extends ProxyCreator {

	public CotrixProxyCreator(JClassType serviceIntf) {
		super(serviceIntf);
	}
	
	@Override
    protected Class<? extends RemoteServiceProxy> getProxySupertype() {
        return CotrixRemoteServiceProxy.class;
    }

}
