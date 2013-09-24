package org.cotrix.engine;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.engine.impl.DefaultAction;

/**
 * An {@link Action} factory, as well as a builder for actions of the form <em>type,op*,instance?</em>.
 * 
 * @author Fabio Simeoni
 *
 */
public class ActionBuilder {

	private final List<String> parts = new ArrayList<String>();
	
	//forces use of factory methods
	private ActionBuilder() {
	}
	
	
	/**
	 * Creates an action from its parts.
	 * @param parts the parts
	 * @return the action
	 */
	public static Action action(String part, String ... parts) {
		List<String> ps = new ArrayList<String>();
		ps.add(part);
		ps.addAll(asList(parts));
		return new DefaultAction(ps);
	}
	
	/**
	 * Creates an action on an instance from its parts.
	 * @param instance the instance
	 * @param parts the parts
	 * @return the action
	 */
	public static Action actionOn(String instance, String part, String ... parts) {
		List<String> ps = new ArrayList<String>();
		ps.add(part);
		ps.addAll(asList(parts));
		return new DefaultAction(ps,instance);
	}
	
	/**
	 * Build an action of a given type.
	 * @param type the type
	 * @return the action builder
	 */
	public static ActionBuilder type(String type) {
		return new ActionBuilder().op(type);
	}
	
	
	
	/**
	 * Builds an action with a given operation.
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
