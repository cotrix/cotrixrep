/**
 * 
 */
package org.cotrix.web.portlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.cotrix.io.utils.CdiProducers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixServletContextListener implements ServletContextListener {
	
	protected Logger logger = LoggerFactory.getLogger(CotrixServletContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("Cotrix context destroyed");
		logger.trace("shutting down the repository");
		CdiProducers.virtualRepository().shutdown();
		logger.trace("repository closed"); 
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("Cotrix context initialized");		
	}

}
