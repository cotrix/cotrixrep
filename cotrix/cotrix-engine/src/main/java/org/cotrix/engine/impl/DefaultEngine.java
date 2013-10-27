package org.cotrix.engine.impl;

import static org.cotrix.action.Action.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.engine.impl.Task.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.common.cdi.Current;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.user.User;

/**
 * Default {@link Engine} implementation
 * 
 * @author Fabio Simeoni
 * 
 */
@ApplicationScoped
public class DefaultEngine implements Engine {

	@Inject
	private @Current
	User user;

	@Inject
	private Instance<EngineDelegate> delegates;

	@Override
	public TaskClause perform(final Action a) {

		notNull("action", a);

		return new TaskClause() {

			@Override
			public <T> TaskOutcome<T> with(Callable<T> task) {

				return perform(a, task);

			}
		};
	}

	private <T> TaskOutcome<T> perform(Action action, Callable<T> callable) {

		Task<T> task = taskFor(callable);
		
		//subset permissions to relevant type and resource
		Collection<Action> permissions = filterForAction(action,user.permissions());
		
		authorize(action,permissions);
		
		//delegate based on action type if required
		for (EngineDelegate delegate : delegates)
			if (delegate.type()==action.type())
				return delegate.perform(action,task,user.id(),permissions);
		
		//otherwise execute directly
		return new TaskOutcome<T>(permissions,task.execute(action));
	}
	
	
	
	//helpers
	
	private void authorize(Action action, Collection<Action> permissions) {
		
		if (!action.included(permissions))
			throw new IllegalAccessError(user + " cannot perform " + action
					+ ", as her permissions allow only "+permissions);
	}
	
	
	private Collection<Action> filterForAction(Action action, Collection<Action> permissions) {
		
		Collection<Action> filtered = new ArrayList<Action>();
		
		for (Action permission : permissions)

			if (permission == allActions) //expand and instantiate wildcard
				for (Action a : action.type()) {
					filtered.add(a.on(action.resource()));
					break;
				}
			else 
				if (action.type()==permission.type())
					filtered.add(
							permission.isTemplate() ? permission.on(action.resource()):permission
					);
			
		
		return filtered;
		
	}

}
