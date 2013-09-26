package org.cotrix.action.impl;

import static java.util.Arrays.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cotrix.action.Action;

/**
 * Default {@link Action} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class DefaultAction implements Action {
	
	private final List<String> parts = new ArrayList<String>();
	
	private final String instance;
	
	/**
	 * Creates an action from its parts.
	 * @param parts the parts
	 */
	public DefaultAction(List<String> parts) {
	
		valid("parts",parts);
		
		this.parts.addAll(parts);
		this.instance=null;
	}
	
	/**
	 * Creates an action from its parts and relatively to an instance. 
	 * @param parts the parts
	 * @param instance the instance
	 */
	public DefaultAction(List<String> parts,String instance) {
		
		valid("instance",instance);
		valid("parts",parts);
		
		this.parts.addAll(parts); //defensive
		this.instance=instance;
	}
	
	@Override
	public List<String> parts() {
		return parts;
	}
	
	public String instance() {
		return instance;
	}
	
	@Override
	public boolean isOnInstance() {
		return instance!=null;
	}
	
	
	@Override
	public boolean isIn(Action... actions) {
		
		return isIn(asList(actions));
	}
	
	@Override
	public boolean isIn(Collection<Action> actions) {
		
		for (Action a : actions)
			if (this.matches(a))
				return true;
		
		return false;
	}
	
	@Override
	public Action cloneFor(String instance) {
		return new DefaultAction(this.parts,instance);
	}
	
	
	
	//helpers   
	
	private boolean matches(Action a) {
		
		if (this.equals(a))
			return true;
		
		boolean thisInstanceDoesNotMatchThatInstance = 
				this.isOnInstance() && 
				!(a.instance()==any || this.instance().equals(a.instance()));
		
		boolean thatInstanceDoesNotMatchThisInstance = 
				a.isOnInstance() && 
				!(a.instance()==any || a.instance().equals(this.instance()));
		
		if (thisInstanceDoesNotMatchThatInstance || thatInstanceDoesNotMatchThisInstance)
			return false;
		
		if (a.isOnInstance() && !(a.instance().equals(any) || a.instance().equals(this.instance())))
			return false;
		
		return matches(a.parts());
		
	}
	
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
		String ps = parts.subList(0, Math.min(parts.size(), maxLen)).toString();
		
		return instance==null?ps:ps+" on "+instance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
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
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		if (parts == null) {
			if (other.parts != null)
				return false;
		} else if (!parts.equals(other.parts))
			return false;
		return true;
	}


	
	
}
