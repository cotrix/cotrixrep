package org.cotrix.engine.impl;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.engine.Action;

public class DefaultAction implements Action {
	
	private final List<String> parts = new ArrayList<String>();
	
	private final String instance;
	
	
	public DefaultAction(List<String> parts) {
		this(parts,null);
	}
	
	public DefaultAction(List<String> parts,String instance) {
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
	public String toString() {
		final int maxLen = 100;
		String ps = parts.subList(0, Math.min(parts.size(), maxLen)).toString();
		
		return instance==null?ps:ps+" on "+instance;
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
