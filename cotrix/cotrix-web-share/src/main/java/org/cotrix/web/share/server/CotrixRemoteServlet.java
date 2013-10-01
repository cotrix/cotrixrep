/**
 * 
 */
package org.cotrix.web.share.server;


import java.lang.reflect.Field;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public abstract class CotrixRemoteServlet extends RemoteServiceServlet {

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void init()
	{
		Object bean = getBean();
		setDelegate(bean);
	}
	
	public void setDelegate(Object delegate)
	{
		try {
			Field delegateField = RemoteServiceServlet.class.getDeclaredField("delegate");
			delegateField.setAccessible(true);
			delegateField.set(this, delegate);
		} catch(Exception e)
		{
			throw new RuntimeException("Error setting delegate",e);
		}
	}

	public abstract Object getBean();

}

