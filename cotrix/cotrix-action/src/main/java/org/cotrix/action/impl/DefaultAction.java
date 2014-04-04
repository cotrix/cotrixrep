package org.cotrix.action.impl;

import static java.util.Arrays.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;

/**
 * Default {@link Action} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public final class DefaultAction implements Action {

	private final List<String> labels = new ArrayList<String>();
	private final ResourceType type;
	private final String resource;
	
	/**
	 * Creates an untyped template.
	 * 
	 * @param labels the labels of the template.
	 */
	public DefaultAction(List<String> labels) {
	
		this(labels,ResourceType.none);
	}
	
	/**
	 * Creates a typed template.
	 * @param labels the labels of the template
	 * @param type the type of the template;
	 */
	public DefaultAction(List<String> parts, ResourceType type) {
	
		this(parts,type,any);
	}
	
	/**
	 * Creates a typed action with given labels over a given resource.
	 * @param labels the labels
	 * @param type the type
	 * @param resource the resource identifier
	 */
	private DefaultAction(List<String> parts, ResourceType type, String resource) {
	
		valid("labels",parts);
		notNull("type",type);
		valid("resource identifier",resource);
		
		this.labels.addAll(parts);
		this.type=type;
		this.resource=resource;
	}
	
	@Override
	public Action on(String instance) {
		return new DefaultAction(labels,type,instance);
	}
	
	@Override
	public ResourceType type() {
		return type;
	}
	
	@Override
	public List<String> labels() {
		return labels;
	}
	
	@Override
	public boolean isTemplate() {
		return resource==any;
	}
	
	@Override
	public String resource() {
		return resource;
	}
	
	
	@Override
	public boolean included(Action... actions) {
		
		return included(asList(actions));
	}
	
	@Override
	public boolean included(Collection<? extends Action> actions) {
		
		for (Action a : actions)
			if (this.isIn(a))
				return true;
		
		return false;
	}
	
	public boolean isIn(Action a) {
		
		return type() == a.type() && matches(a.labels()) && match(resource,a.resource());
	
	}


	//helpers   	
	
	private boolean matches(List<String> parts) {
		
		for (int i =0; i<parts.size(); i++) {
			String part = parts.get(i);
			//longer actions match if made of *-s
			if (i>=labels().size()) {
				if (part!=any)
					return false;
			}
			else //compare matched labels
				if (!match(labels().get(i),part))
					return false;
		}
		
		return true;
			
	}
	
	private boolean match(String s1, String s2) {
		return s1.equals(s2) || any.equals(s2);
	}
	
	@Override
	public String toString() {
		final int maxLen = 100;
		String ps= labels.subList(0, Math.min(labels.size(), maxLen)).toString();
		
		return ps+" on "+resource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((labels == null) ? 0 : labels.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
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
		if (labels == null) {
			if (other.labels != null)
				return false;
		} else if (!labels.equals(other.labels))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


	
	
}
