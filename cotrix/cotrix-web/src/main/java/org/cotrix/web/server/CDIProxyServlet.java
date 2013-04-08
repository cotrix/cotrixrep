package org.cotrix.web.server;
import java.io.IOException;
import java.util.Iterator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.jboss.weld.environment.servlet.Listener;

/**
 * <p>
 * Servlet which proxies to another servlet which was loaded using CDI and thus receives proper dependency injection.
 * You can use this on containers (like GAE) which do not support CDI for Servlets.
 * </p>
 * 
 * <p>
 * Use an init-param which defines the actual class of the servlet to load.
 * </p>
 */
public class CDIProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** */
	public static final String TARGET_SERVLET_CLASS = "targetServletClass";

	/** The actual, CDI-managed servlet we wrap */
	HttpServlet target;

	/** */
	@Override
	@SuppressWarnings("all")
	public void init() throws ServletException {
		
		super.init();
		
		BeanManager mgr = (BeanManager) this.getServletContext().getAttribute(Listener.BEAN_MANAGER_ATTRIBUTE_NAME);
		System.out.println("xxx"+mgr);
		//actual servlet type name
		String typeName = this.getServletConfig().getInitParameter(TARGET_SERVLET_CLASS);
		System.out.println("----->>>"+typeName);
		try {
			System.out.println("******"+typeName);
			Class<?> type = Class.forName(typeName);
			System.out.println(typeName + "xxxx" + type	);
			Iterator<Bean<?>> it = mgr.getBeans(type).iterator();
			if (it.hasNext()) {
				System.out.println("In the loop");
				Bean<?> servletBean = it.next();

				CreationalContext ctx = (CreationalContext) mgr.createCreationalContext(servletBean);
				
				this.target = (HttpServlet) servletBean.create(ctx);
				
				this.target.init(this.getServletConfig());
								
			} 
			else
				throw new Exception("WELD listener is not configured?");
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/** */
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		this.target.service(req, res);
	}
}