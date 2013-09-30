package org.acme;

import static org.junit.Assert.*;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.jboss.weld.context.http.HttpRequestContext;
import org.jboss.weld.context.http.HttpSessionContext;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.jglue.cdiunit.DummyHttpRequest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
@SuppressWarnings("serial")
public class ScopeTest {

	@Inject
	HttpRequestContext reqCtx;
	
	@Inject
	HttpSessionContext sessCtx;
	
	@Inject
	ContextController contextController; 
	
	@Inject 
	Bean bean;
	
	
	@Ignore
	@Test
	public void test() {

		System.out.println("\nnew session");

		HttpServletRequest sess = new DummyHttpRequest();
		
		contextController.openSession(sess);
		
			String sessFingerprint = bean.useSessionBean();
			
			System.out.println("\n new request\n");
		
			HttpServletRequest req = new DummyHttpRequest();
		
			contextController.openRequest(req);
		
				String reqFingerprint = bean.useRequestBean(); 
		
				assertEquals(reqFingerprint,bean.useRequestBean());
				assertEquals(sessFingerprint,bean.useSessionBean());
		
			contextController.closeRequest();
		
			reqCtx.dissociate(req);
		
		
			System.out.println("\n new request\n");
		
			req = new DummyHttpRequest();
		
			contextController.openRequest(req);
		
				assertFalse(reqFingerprint.equals(bean.useRequestBean()));
				assertEquals(sessFingerprint,bean.useSessionBean());
		
			contextController.closeRequest();
	
			reqCtx.dissociate(req);
		
		
		
		contextController.closeSession();
		
		sessCtx.dissociate(sess);
		
		
		
		
		System.out.println("\nnew session");
		
		sess = new DummyHttpRequest();
		
		contextController.openSession(sess);
		
			assertFalse(sessFingerprint.equals(bean.useSessionBean()));
		
		contextController.closeSession();
		
		sessCtx.destroy(sess.getSession());
	}
	
	static class Bean {

		@Inject
		DependentBean dependentbean;
		
		@Inject
		SingletonBean singletonbean;

		@Inject
		AppBean appbean;
		
		@Inject
		SessionBean sessionbean;
		
		@Inject
		RequestBean requestbean;
		
		public Bean() {
			System.out.println("creating "+this.getClass().getSimpleName()+" ...");
		}
		
		@PostConstruct
		public void init() {
			
			System.out.println("\ninitialising "+this.getClass().getSimpleName());
			System.out.println("   injected "+dependentbean.getClass().getSimpleName());
			System.out.println("   injected "+singletonbean.getClass().getSimpleName());
			System.out.println("   injected "+appbean.getClass().getSimpleName());
			System.out.println("   injected "+sessionbean.getClass().getSimpleName());
			System.out.println("----------------------------------------------------------------");
			
			
		}
		
		public String useAppBean() {
			System.out.println("   using "+appbean.getClass().getSimpleName());
			appbean.toString();
			return appbean.toString(); 
		}
		
		public String useSessionBean() {
			System.out.println("   using "+sessionbean.getClass().getSimpleName());
			System.out.println("   ...with "+sessionbean.toString());
			return sessionbean.toString();
		}
		
		public String useRequestBean() {
			System.out.println("   using "+requestbean.getClass().getSimpleName()+" ...");
			System.out.println("   ...with "+requestbean.toString());
			return requestbean.toString();
		}
	}
	
	static class DependentBean {
		
		public DependentBean() {
			System.out.println("   creating "+this.getClass().getSimpleName());
		}
		
		@PostConstruct
		public void init() {
			System.out.println("   initialising "+this.getClass().getSimpleName());
		}
	}
	
	@Singleton
	static class SingletonBean {
		
		public SingletonBean() {
			System.out.println("   creating "+this.getClass().getSimpleName());
		}
		
		@PostConstruct
		public void init() {
			System.out.println("   initialising "+this.getClass().getSimpleName());
		}
	}
	
	
	static @ApplicationScoped class AppBean {
		
		public AppBean() {
			System.out.println("   creating "+this.getClass().getSimpleName());
		}
		
		@PostConstruct
		public void init() {
			System.out.println("     initialising "+this.getClass().getSimpleName());
		}
	}
	
	
	static @SessionScoped class SessionBean implements Serializable {
		
		public SessionBean() {
			System.out.println("   creating "+this.getClass().getSimpleName());
		}
		
		@PostConstruct
		public void init() {
			System.out.println("      initialising "+this.getClass().getSimpleName());
		}
	}
	
	
	static @RequestScoped class RequestBean implements Serializable {
		
		int i=0;
		
		public RequestBean() {
			System.out.println("   creating "+this.getClass().getSimpleName());
		}
		
		@PostConstruct
		public void init() {
			System.out.println("      initialising "+this.getClass().getSimpleName());
		}
	}
	
}
