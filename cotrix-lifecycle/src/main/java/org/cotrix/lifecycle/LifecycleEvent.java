package org.cotrix.lifecycle;

/**
 * An event raised when an {@link Action} is performed on a given State of a given lifecycle {@link Lifecycle}
 * @author Fabio Simeoni
 *
 */
public class LifecycleEvent {

	private final State origin;
	private final State target;
	private final Action action;
	private final String resourceId;
	
	/**
	 * Creates an instance that models the a transaction
	 * @param resourceId
	 * @param origin
	 * @param action
	 * @param target
	 */
	public LifecycleEvent(String resourceId, State origin, Action action, State target) {
		this.origin=origin;
		this.target=target;
		this.action=action;
		this.resourceId=resourceId;
	}
	
	public Action action() {
		return action;
	}
	
	public State origin() {
		return origin;
	}
	
	public State target() {
		return target;
	}
	
	public String resourceId() {
		return resourceId;
	}
	
	

	@Override
	public String toString() {
		return "LifecycleEvent [origin=" + origin + ", target=" + target + ", action=" + action + ", resourceId="
				+ resourceId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		LifecycleEvent other = (LifecycleEvent) obj;
		if (action != other.action)
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (resourceId == null) {
			if (other.resourceId != null)
				return false;
		} else if (!resourceId.equals(other.resourceId))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	
}
