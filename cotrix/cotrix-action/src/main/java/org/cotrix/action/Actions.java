package org.cotrix.action;

import static java.util.Arrays.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cotrix.action.impl.DefaultAction;

/**
 * An {@link Action} factory, as well as a builder for actions of the form <em>type,op*,instance?</em>.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class Actions {	
	
	/**
	 * Creates an action from its labels.
	 * @param label the first label
	 * @param labels the remaining labels, if any
	 * @return the action
	 */
	public static Action action(String label, String ... labels) {
		
		List<String> ps = new ArrayList<String>();
		ps.add(label);
		ps.addAll(asList(labels));
		return new DefaultAction(ps);
	}
	
	/**
	 * Creates an action from its labels.
	 * @param label the first label
	 * @param labels the remaining labels, if any
	 * @return the action
	 */
	public static Action action(ResourceType type, String label, String ... labels) {
		
		List<String> ps = new ArrayList<String>();
		ps.add(label);
		ps.addAll(asList(labels));
		return new DefaultAction(ps,type);
	}
	
	/**
	 * Restricts a set of actions to those that have the same type as a given action.
	 * <p>
	 * In the process, it instantiates action templates to the resource of the given action.
	 *  
	 * @param action the given action
	 * @param actions the input actions
	 * @return the filtered actions
	 */
	public static Collection<Action> filterForAction(Action action, Collection<Action> actions) {
		
		notNull("actions",actions);
		notNull("action",action);
		
		Collection<Action> filtered = new ArrayList<Action>();
		
		for (Action a : actions)

			if (action.type()==a.type())
					filtered.add(
							a.isTemplate() ? a.on(action.resource()):a
					);
			
		return filtered;
		
	}
}
