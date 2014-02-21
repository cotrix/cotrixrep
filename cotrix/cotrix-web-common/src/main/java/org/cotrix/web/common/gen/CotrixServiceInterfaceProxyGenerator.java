/**
 * 
 */
package org.cotrix.web.common.gen;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixServiceInterfaceProxyGenerator extends ServiceInterfaceProxyGenerator {
	
	   @Override
	    protected ProxyCreator createProxyCreator(JClassType remoteService) {
	        return new CotrixProxyCreator(remoteService);
	    }

}
