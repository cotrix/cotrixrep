package org.cotrix.engine;

import static java.util.Arrays.*;
import static org.cotrix.engine.Action.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.engine.impl.DefaultAction;

/**
 * An {@link Action} factory and a builder for three-part actions of the form <em>type:op:instance?</em>.
 * 
 * @author Fabio Simeoni
 *
 */
public class ActionBuilder {

	private final List<String> parts = new ArrayList<String>();
	
	private ActionBuilder() {
	}
	
	/**
	 * Creates an action from its parts.
	 * @param parts the parts
	 * @return the action
	 */
	public static Action any() {
		return type(any).end();
	}
	
	/**
	 * Creates an action from its parts.
	 * @param parts the parts
	 * @return the action
	 */
	public static Action action(String ... parts) {
		return new DefaultAction(asList(parts));
	}
	
	/**
	 * Creates an action over an instance from its parts.
	 * @param instance the instance
	 * @param parts the parts
	 * @return the action
	 */
	public static Action action(String instance, String ... parts) {
		return new DefaultAction(asList(parts),instance);
	}
	
	/**
	 * Build an action of a given type
	 * @param type the type
	 * @return the action builder
	 */
	public static ActionBuilder type(String type) {
		return new ActionBuilder().op(type);
	}
	
	
	
	/**
	 * Builds an action with a given operation
	 * @param op the operation
	 * @return this builder
	 */
	public ActionBuilder op(String op) {
		this.parts.add(op);
		return this;
	}
	
	/**
	 * Returns an action on a given instance.
	 * @param instance the instance
	 * @return the action
	 */
	public Action on(String instance) {
		return new DefaultAction(parts,instance);
	}
	
	public Action end() {
		return new DefaultAction(parts);
	}

}
