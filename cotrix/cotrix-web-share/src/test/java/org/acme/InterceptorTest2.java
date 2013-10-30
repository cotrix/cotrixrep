package org.acme;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class InterceptorTest2 {

	@Inject
	Intercepted bean;
	
	@Test
	public void test() throws Exception {
		
		bean.method();
		
	}
	
	
	@InterceptorBinding
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	static public @interface InterceptMe {}
	
	
	@InterceptMe
	@Interceptor @Priority(1)
	static class AnInterceptor {
		
		@Inject
		ScopedBean delegate;
		
		@AroundInvoke
		public Object intercept(final InvocationContext ctx) throws Exception {
			
			return delegate.execute(ctx);
			
		}
	}
	
	@ApplicationScoped
	static class ScopedBean {
		
		public Object execute(InvocationContext ctx) throws Exception {
			return ctx.proceed();
		}
	}
	
	@InterceptMe
	static class Intercepted {
		
		public void method() {
			System.out.println(this+" is doing it");
		}
		
	}
}
