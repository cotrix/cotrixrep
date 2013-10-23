package org.cotrix.action.impl;

import static java.util.Arrays.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.action.InstanceAction;

/**
 * Default {@link Action} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class DefaultAction extends AbstractAction implements Action {
	
	private final List<String> parts = new ArrayList<String>();
	
	/**
	 * Creates an action from its parts.
	 * @param parts the parts
	 */
	public DefaultAction(List<String> parts) {
	
		valid("parts",parts);
		
		this.parts.addAll(parts);
	}
	
	@Override
	public List<String> parts() {
		return parts;
	}
	
	@Override
	public boolean isIn(Action... actions) {
		
		return isIn(asList(actions));
	}
	
	@Override
	public boolean isIn(Collection<Action> actions) {
		
		for (Action a : actions)
			if (this.isIn(a))
				return true;
		
		return false;
	}
	
	@Override
	public InstanceAction on(String instance) {
		return new DefaultInstanceAction(this, instance);
	}
	
	
	public boolean isIn(Action a) {
		
		if (this.equals(a))
			return true;
		
		//must be generic and match 
		return !(a instanceof InstanceAction) && matches(a.parts());
	}	


	//helpers   
	
	
	private boolean matches(List<String> parts) {
		
		for (int i =0; i<parts.size(); i++) {
			String part = parts.get(i);
			//longer actions match if made of *-s
			if (i>=this.parts.size()) {
				if (part!=any)
					return false;
			}
			else //compare matched parts
				if (!matches(this.parts.get(i),part))
					return false;
		}
		
		return true;
			
	}
	
	private boolean matches(String thisPart, String superPart) {
		return thisPart.equals(superPart) || superPart==any;
	}
	
	
	@Override
	public String toString() {
		final int maxLen = 100;
		return parts.subList(0, Math.min(parts.size(), maxLen)).toString();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parts == null) ? 0 : parts.hashCode());
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
		DefaultAction other = (DefaultAction) obj;
		if (parts == null) {
			if (other.parts != null)
				return false;
		} else if (!parts.equals(other.parts))
			return false;
		return true;
	}


	
	
}
