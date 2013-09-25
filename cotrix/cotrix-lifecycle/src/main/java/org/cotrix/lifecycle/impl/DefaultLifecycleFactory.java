package org.cotrix.lifecycle.impl;

import static org.cotrix.action.CodelistActions.*;
import static org.cotrix.lifecycle.impl.DefaultLifecycleStates.*;
import static org.cotrix.lifecycle.utils.Utils.*;

import org.cotrix.action.Action;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleFactory;
import org.cotrix.lifecycle.State;

import com.googlecode.stateless4j.StateMachine;

/**
 * Default {@link Lifecycle} factory. 
 * @author Fabio Simeoni
 */
public class DefaultLifecycleFactory implements LifecycleFactory {

	
	private static final long serialVersionUID = 1L;

	@Override
	public String name() {
		return LifecycleFactory.DEFAULT;
	}

	@Override
	public Lifecycle create(String id) {
		
		valid("resource identifier",id);
		
		try {
			return new S4JLifecycle(name(), id, machine(id));
		} catch (Exception e) {
			throw new RuntimeException("error creating lifecycle", e);
		}
	}

	//helper
	private StateMachine<State, Action> machine(String id) throws Exception {
		
		StateMachine<State, Action> machine = new StateMachine<State, Action>(draft);
		
		machine.Configure(draft)
						.PermitReentry(edit.cloneFor(id))
					    .Permit(lock.cloneFor(id),locked)
					    .Permit(seal.cloneFor(id),sealed);
		
		machine.Configure(locked)
						.Permit(unlock.cloneFor(id), draft);
		
		return machine;
	}


}
