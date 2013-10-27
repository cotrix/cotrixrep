package org.cotrix.action;

import static java.util.Arrays.*;

import java.util.ArrayList;
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
	public static Action action(ActionType type, String label, String ... labels) {
		
		List<String> ps = new ArrayList<String>();
		ps.add(label);
		ps.addAll(asList(labels));
		return new DefaultAction(ps,type);
	}
	
}
