package org.cotrix.web.common.server.async;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.cotrix.common.async.TaskManagerProvider;
import org.cotrix.web.common.server.util.HttpServletRequestHolder;
import org.jboss.weld.context.http.HttpSessionContext;

@ApplicationScoped @Alternative 
@Priority(RUNTIME)
public class HttpTaskManagerProvider implements TaskManagerProvider {
	
	@Inject 
	HttpSessionContext httpContext;
	
	@Inject
	private HttpServletRequestHolder holder;


	public class HttpTaskManager implements TaskManager {
		
		final HttpServletRequest request;
		
		public HttpTaskManager(HttpServletRequest req) {
			this.request = req;
		}
		
		@Override
		public void started() {
		
			httpContext.associate(request);
			httpContext.activate();
			
		}
		
		@Override
		public void finished() {
			
			httpContext.dissociate(request);
		
		}
	}
	
	@Override
	public TaskManager get() {
	
		return new HttpTaskManager(holder.getRequest());
	}
	
	
}
