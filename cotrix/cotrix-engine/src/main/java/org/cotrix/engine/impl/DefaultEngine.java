package org.cotrix.engine.impl;

import static org.cotrix.action.Actions.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.engine.impl.Task.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.common.BeanSession;
import org.cotrix.common.events.Current;
import org.cotrix.domain.user.User;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;

/**
 * Default {@link Engine} implementation
 * 
 * @author Fabio Simeoni
 * 
 */
@ApplicationScoped
public class DefaultEngine implements Engine {

	@Inject @Current
	private BeanSession session;

	@Inject
	private List<EngineDelegate> delegates;
	
	
	@Override
	public TaskClause perform(final Action a) {

		notNull("action", a);

		return new TaskClause() {

			@Override
			public <T> TaskOutcome<T> with(Callable<T> task) {

				return perform(a, taskFor(task));

			}
		};
	}

	private <T> TaskOutcome<T> perform(Action action, Task<T> task) {
		
		User user = session.get(User.class);
		
		//subset permissions to relevant type and resource
		Collection<Action> permissions = filterForAction(action,user.permissions());
		
		if (!action.included(permissions))
			throw new IllegalAccessError(user + " cannot perform " + action
					+ ", as her permissions allow only "+permissions);
		
		//delegate based on action type if required
		for (EngineDelegate delegate : delegates)
			if (delegate.type()==action.type())
				return delegate.perform(action,task,user.id(),permissions);
		
		//otherwise execute directly
		return new TaskOutcome<T>(permissions,task.execute(action));
	}

	
	@Produces
	static List<EngineDelegate> produces(Instance<EngineDelegate> ds) {
		
		List<EngineDelegate> delegates = new ArrayList<EngineDelegate>();
		
		for (EngineDelegate delegate : ds)
			delegates.add(delegate);
		
		return delegates;
	}
}
