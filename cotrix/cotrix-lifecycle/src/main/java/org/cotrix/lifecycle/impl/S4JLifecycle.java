package org.cotrix.lifecycle.impl;

import static org.cotrix.common.Utils.*;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceAction;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleEvent;
import org.cotrix.lifecycle.State;

import com.googlecode.stateless4j.StateMachine;

/**
 * An implementatin of {@link Lifecycle} based on Google's Stateless4j.
 * @author Fabio Simeoni
 *
 */
public class S4JLifecycle extends AbstractLifecycle {
	
	private static final long serialVersionUID = 1L;

	private final StateMachine<State,Action> machine;

	/**
	 * Creates an instance with a given name, for a given resource, and with given {@link StateMachine} representation
	 * @param name the name of the lifecycle
	 * @param id the identifier of the resource
	 * @param machine the state machine representation
	 */
	public S4JLifecycle(String name,String id,StateMachine<State,Action> machine) {

		super(name,id);

		notNull("state machine",machine);
		this.machine=machine;
	}
	
	@Override
	public State state() {
		return machine.getState();
	}
	
	@Override
	public boolean allows(Action action) {
		
		notNull("action",action);
		
		
		return machine.CanFire(generic(action));
	}
	
	@Override
	public void notify(Action action) {
		
		notNull("action",action);
		
		action = generic(action);
		
		try {
			State origin = state();
			machine.Fire(action);
			State target = state();
			eventProducer().fire(new LifecycleEvent(id, origin, action,target));
		}
		catch(Exception e) {
			throw new IllegalStateException(action.toString());
		}
	}
	
	@Override
	public Collection<Action> allowed() {
		return machine.getPermittedTriggers();
	}
	

	//helpers
	
	private Action generic(Action a) {
		return a instanceof ResourceAction? ResourceAction.class.cast(a).onAny():a;
	}
}
