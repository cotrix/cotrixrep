package org.cotrix.lifecycle.impl;

import static java.util.Arrays.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.lifecycle.impl.DefaultLifecycleStates.*;

import org.cotrix.action.Action;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleFactory;
import org.cotrix.lifecycle.State;

import com.googlecode.stateless4j.StateMachine;

/**
 * Default {@link Lifecycle} factory. 
 * @author Fabio Simeoni
 */
public class S4JLifecycleFactory implements LifecycleFactory {


	private static final long serialVersionUID = 1L;

	@Override
	public String name() {
		return LifecycleFactory.DEFAULT;
	}

	@Override
	public Lifecycle create(String id) {

		return create(id,draft);
	}
	
	@Override
	public Lifecycle create(String id, State startState) {
		
		valid("resource identifier",id);
		notNull("state",startState);
		

		StateMachine<State,Action> machine = null;
		
		if (!asList(DefaultLifecycleStates.values()).contains(startState))
			throw new IllegalArgumentException("invalid state "+startState+" for this lifecycle");
		
		try {
		
			machine = machine(id,startState);
		
		} catch (Exception e) {
			throw new RuntimeException("error creating lifecycle", e);
		}
		
		return new S4JLifecycle(name(), id, machine);
	}

	//helper
	private StateMachine<State, Action> machine(String id, State state) throws Exception {

		StateMachine<State, Action> machine = new StateMachine<State, Action>(state);

		machine.Configure(draft)
								.PermitReentry(VIEW.on(id))
								.PermitReentry(EDIT.on(id))
								.Permit(LOCK.on(id),locked);

		machine.Configure(locked)
								.PermitReentry(VIEW.on(id))
								.Permit(SEAL.on(id),sealed)
								.Permit(UNLOCK.on(id), draft);
		
		machine.Configure(sealed)
								.PermitReentry(VIEW.on(id));


		return machine;
	}


}
