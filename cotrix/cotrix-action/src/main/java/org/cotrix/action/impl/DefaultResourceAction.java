package org.cotrix.action.impl;

import static org.cotrix.common.Utils.*;

import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceAction;

/**
 * Default {@link ResourceAction} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public final class DefaultResourceAction extends AbstractAction implements ResourceAction {

	private final Action action;
	private final String resource;
	
	/**
	 * Creates an action from its parts and relatively to an resource. 
	 * @param parts the parts
	 * @param resource the resource
	 */
	public DefaultResourceAction(Action action,String resource) {
		
		notNull("action",action);
		notNull("resource",resource);
		
		this.action=action;
		this.resource=resource;
	}
	
	@Override
	public Class<? extends Action> type() {
		return getClass();
	}
	
	@Override
	public String resource() {
		return resource;
	}
	
	@Override
	public List<String> parts() {
		return action.parts();
	}
	
	@Override
	public ResourceAction on(String instance) {
		return new DefaultResourceAction(action, instance);
	}
	
	@Override
	public boolean isIn(Action a) {
		
		if (a instanceof ResourceAction) {
			
			ResourceAction instanceAction = ResourceAction.class.cast(a);
			Action generic = instanceAction.onAny();
			
			return action.isIn(generic) && this.resource.equals(instanceAction.resource());
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
		return action.toString()+" on "+resource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
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
		DefaultResourceAction other = (DefaultResourceAction) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}
	
	
}
