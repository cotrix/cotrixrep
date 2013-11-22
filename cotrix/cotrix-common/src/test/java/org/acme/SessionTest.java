package org.acme;

import static junit.framework.Assert.*;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.jglue.cdiunit.DummyHttpRequest;
import org.jglue.cdiunit.ProducesAlternative;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("serial")
@RunWith(CdiRunner.class)
public class SessionTest {

	@Inject @Current
	BeanSession session;
	
	@Inject
	Bean bean;
	
	@Inject
	ContextController scopes;
	
	@Produces @SessionScoped
	public static @Current BeanSession session() {
		
		return new BeanSession();		
	}
	
	@Test
	public void sessionScenario() {

		scopes.openRequest(new DummyHttpRequest());
		
		//producer cannot produce yet
		try {
			bean.toString();
			fail();
		}
		catch(IllegalStateException e){}
		
		//create data to be produced
		session.add(Bean.class,new Bean());
		
		//access now succeeds
		bean.toString();
		
		scopes.closeRequest();
		
		scopes.closeSession();
		
	}
	
	@Produces @ProducesAlternative @SessionScoped
	Bean sessionBeanProducer(@Current BeanSession session) {
		
		return session.get(Bean.class);
	}
	
	static class Bean implements Serializable {}
}
