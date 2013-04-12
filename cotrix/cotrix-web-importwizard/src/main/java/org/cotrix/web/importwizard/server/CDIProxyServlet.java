package org.cotrix.web.importwizard.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.jboss.weld.environment.servlet.Listener;

import com.google.gwt.user.client.Window;

/**
 * <p>
 * Servlet which proxies to another servlet which was loaded using CDI and thus
 * receives proper dependency injection. You can use this on containers (like
 * GAE) which do not support CDI for Servlets.
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
		BeanManager mgr = (BeanManager) this.getServletContext().getAttribute(
				Listener.BEAN_MANAGER_ATTRIBUTE_NAME);

		// actual servlet type name
		String typeName = this.getServletConfig().getInitParameter(
				TARGET_SERVLET_CLASS);
		Set<Bean<?>> allBeans = mgr.getBeans(Object.class);
		Iterator<Bean<?>> it2 = allBeans.iterator();
		while (it2.hasNext()) {
			System.out.println("aaaaaaaaaaaaaaaaaaaaaa");
			Bean<?> b = it2.next();
			System.out.println(b.getBeanClass());
		}
		
		try {
			Class<?> type = Class.forName(typeName);
			System.out.println("type ::: " + type.getName());
			Iterator<Bean<?>> it = mgr.getBeans(type).iterator();
			System.out.println("*****xxx****" + type);
			if (it.hasNext()) {

				Bean<?> servletBean = it.next();
				System.out.println("*********" + servletBean);

				CreationalContext ctx = (CreationalContext) mgr
						.createCreationalContext(servletBean);

				this.target = (HttpServlet) servletBean.create(ctx);

				this.target.init(this.getServletConfig());

			} else
				throw new Exception("WELD listener is not configured?");

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/** */
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		this.target.service(req, res);
	}
}