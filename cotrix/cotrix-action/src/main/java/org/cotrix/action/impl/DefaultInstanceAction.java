package org.cotrix.action.impl;

import static org.cotrix.common.Utils.*;

import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.action.InstanceAction;

/**
 * Default {@link InstanceAction} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class DefaultInstanceAction extends AbstractAction implements InstanceAction {

	private final Action action;
	private final String instance;
	
	/**
	 * Creates an action from its parts and relatively to an instance. 
	 * @param parts the parts
	 * @param instance the instance
	 */
	public DefaultInstanceAction(Action action,String instance) {
		
		notNull("action",action);
		notNull("instance",instance);
		
		this.action=action;
		this.instance=instance;
	}
	
	@Override
	public String instance() {
		return instance;
	}
	
	@Override
	public List<String> parts() {
		return action.parts();
	}
	
	@Override
	public InstanceAction on(String instance) {
		return new DefaultInstanceAction(action, instance);
	}
	
	@Override
	public boolean isIn(Action a) {
		
		if (a instanceof InstanceAction) {
			
			InstanceAction instanceAction = InstanceAction.class.cast(a);
			Action generic = instanceAction.onAny();
			
			return action.isIn(generic) && this.instance.equals(instanceAction.instance());
		}
		else
			return action.isIn(a);
	}
	
	@Override
	public Action onAny() {
		return action;
	}
	
	@Override
	public String toString() {
		return action.toString()+" on "+instance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultInstanceAction other = (DefaultInstanceAction) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		return true;
	}
	
	
}
