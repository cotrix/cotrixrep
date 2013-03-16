package org.cotrix;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;
import org.cotrix.repository.utils.QueryFactoryInjector;
import org.junit.Test;

public class InjectionTest {

	@Test
	public void queryFactoryIsInjected() {
		
		
		ContainerLifecycle container = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
		container.startApplication(null);
		container.getBeanManager().getBeans(QueryFactoryInjector.class).iterator().next().create(null);
		container.stopApplication(null);
        
		
	}
}


