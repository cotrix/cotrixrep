package org.cotrix.action.impl;

import static java.util.Arrays.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceAction;

/**
 * Default {@link Action} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public final class DefaultAction extends AbstractAction implements Action {
	
	private final List<String> parts = new ArrayList<String>();
	private final Class<? extends Action> type;

	/**
	 * Creates an action with given parts.
	 * @param parts the parts
	 */
	public DefaultAction(List<String> parts) {
	
		this(parts,DefaultAction.class);
	}
	
	/**
	 * Creates an action with given parts and a given type .
	 * @param parts the parts
	 * @param type the type;
	 */
	public DefaultAction(List<String> parts, Class<? extends Action> type) {
	
		valid("parts",parts);
		
		this.parts.addAll(parts);
		
		this.type=type;
	}
	
	@Override
	public Class<? extends Action> type() {
		return type;
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
	public ResourceAction on(String instance) {
		return new DefaultResourceAction(this, instance);
	}
	
	
	public boolean isIn(Action a) {
		
		if (this.equals(a))
			return true;
		
		//must either be any or must have same type and matching parts 
		return a==allActions || (this.type()==a.type() && matches(a.parts()));
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
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	
	
	



	
	
}
