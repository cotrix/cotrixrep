/**
 * 
 */
package org.cotrix.web.share.server.task;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.cotrix.action.Action;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.User;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.web.share.shared.exception.IllegalActionException;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
import org.cotrix.web.share.shared.feature.Request;
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

	@Inject
	@Current
	User user;

	@AroundInvoke
	public Object manageTask(final InvocationContext ctx) throws Exception {
		
		Method method = ctx.getMethod();
		
		logger.trace("manageTask method: {}", method.getName());

		Annotation taskAnnotation = getTaskAnnotation(method);
		
		logger.trace("action: {} user: {}", taskAnnotation, user);

		if (taskAnnotation ==null)
			return ctx.proceed();
		
		Callable<Object> task = new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				return ctx.proceed();
			}
		};

		Action action = getAction(taskAnnotation);
		
		String resource = getIdentifier(ctx.getMethod().getParameterAnnotations(), ctx.getParameters());

		if (resource!=null) {
			
			logger.trace("instance: {}", resource);
			
			action = action.on(resource);
		}
		
		try {
			TaskOutcome<Object> outcome = engine.perform(action).with(task);
			
			Object output = outcome.output();
	
			if (output instanceof FeatureCarrier) {
				FeatureCarrier response = (FeatureCarrier) output;
				actionMapper.fillFeatures(response, resource, outcome.nextActions());
			}
	
			return output;
		} catch(IllegalAccessError e) {
			throw new IllegalActionException(e.getMessage());
		}

	}

	protected String getIdentifier(Annotation[][] parametersAnnotations, Object[] parameters) {
		if (parameters == null || parameters.length == 0)
			return null;

		String id = getIdentifierAsRequest(parameters);
		if (id != null)
			return id;
		return getIdentifierFromAnnotated(parametersAnnotations, parameters);
	}

	protected String getIdentifierAsRequest(Object[] parameters) {
		if (parameters == null || parameters.length == 0)
			return null;
		for (Object parameter : parameters) {
			if (parameter instanceof Request) {
				Request<?> request = (Request<?>) parameter;
				return request.getId();
			}
		}
		return null;
	}

	protected String getIdentifierFromAnnotated(Annotation[][] parametersAnnotations, Object[] parameters) {
		for (int i = 0; i < parametersAnnotations.length; i++) {
			Annotation[] parameterAnnotations = parametersAnnotations[i];
			for (Annotation annotation : parameterAnnotations) {
				if (annotation.annotationType().equals(Id.class))
					return String.valueOf(parameters[i]);
			}
		}

		return null;
	}

	//helper
	
	private Annotation getTaskAnnotation(Method m) {
		
		for (Annotation a : m.getAnnotations())
			if (a.annotationType().isAnnotationPresent(Task.class))
			return a;
		
		return null;
	}
	
	private Action getAction(Annotation a) {
		
		try {
			return Action.class.cast(a.getClass().getMethods()[0].invoke(a));
		}
		catch(Exception e) {
			throw new RuntimeException("invalid Task annotation "+a+": first parameter is not an action");
		}
	}
}
