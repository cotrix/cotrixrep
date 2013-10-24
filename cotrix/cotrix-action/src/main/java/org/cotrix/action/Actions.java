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
	 * @see Action#any
	 */
	public static final String any = Action.any; //convenience to avoid multiple imports
	
	public static final Action allActions = action(any); 
	
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
	 * Creates an action from its parts.
	 * @param parts the parts
	 * @return the action
	 */
	public static Action action(Class<? extends Action> type, String part, String ... parts) {
		
		List<String> ps = new ArrayList<String>();
		ps.add(part);
		ps.addAll(asList(parts));
		return new DefaultAction(ps,type);
	}
	
}
