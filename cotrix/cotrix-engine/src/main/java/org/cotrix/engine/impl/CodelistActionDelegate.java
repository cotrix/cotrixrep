package org.cotrix.engine.impl;

import static org.cotrix.action.ResourceType.*;

import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.action.ResourceType;
import org.cotrix.action.Action;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleService;

@Singleton
public class CodelistActionDelegate implements EngineDelegate {

	@Inject
	private LifecycleService lcService;
	
	@Override
	public ResourceType type() {
		return codelists;
	}
	
	public <T> TaskOutcome<T> perform(Action action, Task<T> task, String user, Collection<Action> permissions) {
	
		String resource = action.resource();
		
		Lifecycle lifecycle = lcService.lifecycleOf(resource);

		Collection<Action> allowed = lifecycle.allowed();
		
		//filter action through lifecycle
		if (!action.included(allowed))
			throw new IllegalStateException(user+" cannot perform " + action + ", as codelist "
					+ lifecycle.resourceId() + " is in state " + lifecycle.state() + " and its lifecycle allows only "
					+ lifecycle.allowed());

		
		//execute task
		T outcome = task.execute(action);
		
		//notify lifecycle
		lifecycle.notify(action);
		
		//compute next actions
		Iterator<Action> it = permissions.iterator();
		
		while (it.hasNext()) {
		
			Action permission = it.next();
			
			if (!permission.included(allowed))
				it.remove();
		}
		
		return new TaskOutcome<T>(permissions,outcome);
	}
	
}
