/**
 * 
 */
package org.cotrix.web.share.server.task;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.cotrix.action.Action;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.web.share.shared.feature.Request;
import org.cotrix.web.share.shared.feature.Response;
import org.cotrix.web.share.shared.feature.UIFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ContainsTask
@Interceptor
public class TaskInterceptor {

	protected Logger logger = LoggerFactory.getLogger(TaskInterceptor.class);

	@Inject
	Engine engine;
	
	@Inject
	ActionMapper actionMapper;

	@AroundInvoke
	public Object manageTask(final InvocationContext ctx) throws Exception { 
		Method method = ctx.getMethod();
		logger.trace("manageTask method: {}", method.getName());

		Task taskAnnotation = method.getAnnotation(Task.class);
		logger.trace("action: {}", taskAnnotation);
		
		if (taskAnnotation!=null) {

			Callable<Object> task = new Callable<Object>() {

				@Override
				public Object call() throws Exception {
					Object result = ctx.getMethod().invoke(ctx.getTarget(), ctx.getParameters());
					return result;
				}
			};

			Action action = taskAnnotation.value();
			String codelistId = getIdentifier(ctx.getMethod().getParameterAnnotations(), ctx.getParameters());
			
			
			logger.trace("codelist id: {}", codelistId);
			if (codelistId!=null) action = action.on(codelistId);

			TaskOutcome<Object> outcome = engine.perform(action).with(task);
			Object output = outcome.output();

			if (output instanceof Response<?>) {
				Response<?> response = (Response<?>) output;
				Set<UIFeature> features = actionMapper.mapActions(outcome.nextActions());
				if (codelistId == null) {
					logger.trace("setting application features set in response: {}", features);
					response.setApplicationFeatures(features);
				} else {
					logger.trace("setting codelist features set in response: {}", features);
					Map<String, Set<UIFeature>> codelistsFeatures = new HashMap<String, Set<UIFeature>>();
					codelistsFeatures.put(codelistId, features);
					response.setCodelistsFeatures(codelistsFeatures);
				}
			}

			return output;

		} else {
			Object result = ctx.getMethod().invoke(ctx.getTarget(), ctx.getParameters());
			System.out.println("INVOKED "+ctx.getMethod().getName());
			return result;
		}

	}

	protected String getIdentifier(Annotation[][] parametersAnnotations, Object[] parameters)
	{
		if (parameters == null || parameters.length == 0) return null;
		
		String id = getIdentifierAsRequest(parameters);
		if (id!=null) return id;
		return getIdentifierFromAnnotated(parametersAnnotations, parameters);
	}
	
	protected String getIdentifierAsRequest(Object[] parameters)
	{
		if (parameters == null || parameters.length == 0) return null;
		for (Object parameter:parameters) {
			if (parameter instanceof Request) {
				Request<?> request = (Request<?>) parameter;
				return request.getId();
			}
		}
		return null;
	}
	
	protected String getIdentifierFromAnnotated(Annotation[][] parametersAnnotations, Object[] parameters)
	{
		for (int i = 0; i < parametersAnnotations.length; i++) {
			Annotation[] parameterAnnotations = parametersAnnotations[i];
			for (Annotation annotation:parameterAnnotations) {
				if (annotation.annotationType().equals(Id.class)) return String.valueOf(parameters[i]);
			}
		}
		
		return null;
	}

}
