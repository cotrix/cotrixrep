package org.cotrix.lifecycle.impl;

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
						.PermitReentry(VIEW.value())
						.PermitReentry(EDIT.value())
					    .Permit(LOCK.value(),locked)
					    .Permit(SEAL.value(),sealed);
		
		machine.Configure(locked)
					.PermitReentry(VIEW.value())
						.Permit(UNLOCK.value(), draft);
		
		return machine;
	}


}
