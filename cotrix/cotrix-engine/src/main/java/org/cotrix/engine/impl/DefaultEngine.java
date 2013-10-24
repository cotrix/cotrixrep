package org.cotrix.engine.impl;

import static org.cotrix.common.Utils.*;
import static org.cotrix.engine.impl.Task.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.action.CodelistAction;
import org.cotrix.action.GenericAction;
import org.cotrix.action.InstanceAction;
import org.cotrix.common.cdi.Current;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.user.User;

/**
 * Default {@link Engine} implementation
 * 
 * @author Fabio Simeoni
 *
 */
@ApplicationScoped
public class DefaultEngine implements Engine {

	private final User user;
	private final LifecycleService lcService;
	
	@Inject
	public DefaultEngine(@Current User user,LifecycleService lcService) {
		this.user=user;
		this.lcService=lcService;
	}
	
	@Override
	public TaskClause perform(final Action a) {
		
		notNull("action",a);
		
		return new TaskClause() {
			
			@Override
			public <T> TaskOutcome<T> with(Callable<T> task) {
				
				return a instanceof InstanceAction ? performOnInstance(InstanceAction.class.cast(a),task) : performGeneric(a, task);
				
			}
		};
	}
	
	public <T> TaskOutcome<T> performOnInstance(final InstanceAction a, Callable<T> task) {
		
		Action generic = a.onAny();
		if (generic instanceof CodelistAction)
			return performOnCodelist(a, task);
		else 
			//extend cases for future instance types
			throw new IllegalArgumentException("unknown instance action "+a);
	}
	
	//helpers
	
	private <T> TaskOutcome<T> performOnCodelist(InstanceAction action, Callable<T> task) {
		
		Lifecycle lifecycle = lcService.lifecycleOf(action.instance());
			
		if (!action.isIn(lifecycle.allowed()))
			throw new IllegalStateException(user.id()+" cannot perform "+action+", as the instance "+ lifecycle.resourceId()+" is in state "+lifecycle.state()+" and its lifecycle allows only "+lifecycle.allowed());
		
		Collection<Action> permissions = user.permissions();
		
		T output =  perform(action, task,permissions);
		
		lifecycle.notify(action);
		
		//contextualise the lifecycle action to instance;
		Collection<Action> allowed = new ArrayList<Action>();
		for (Action a : lifecycle.allowed())
			allowed.add(a.on(action.instance()));

		//build next actions filtering by current user's permissions
		Collection<Action> next = new ArrayList<Action>();

		for (Action permission : permissions)
			//app-level action
			if (permission instanceof GenericAction ||
					//codelist action on Any
					(permission instanceof CodelistAction && permission.on(action.instance()).isIn(allowed)) ||
					//any other action
					permission.isIn(allowed)
			)
				next.add(permission);
		
		
		return new TaskOutcome<T>(next, output);
		
	}

	private <T> TaskOutcome<T> performGeneric(Action action, Callable<T> callable) {
		
		Collection<Action> permissions = user.permissions();
		
		T output =  perform(action,callable,permissions);
		
		return new TaskOutcome<T>(permissions, output);
		
	}
	
	private <T> T perform(Action action, Callable<T> callable, Collection<Action> permissions) {
		
		if (!action.isIn(permissions))
				throw new IllegalAccessError(user.id()+" cannot perform "+action+", as her permissions don't allow it");
		
		
		Task<T> task = taskFor(callable); 
		
		return  task.execute(action,user);
		
	}
}
