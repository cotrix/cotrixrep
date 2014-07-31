package org.cotrix.application.shared;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.utils.DomainUtils.*;

public class DefaultEditorialEvent implements EditorialEvent,Comparable<EditorialEvent> {
	
	private String title; 
	private String subtitle; 
	private String description;
	
	private String user;
	private String timestamp;
	
	private final Type type;

	
	public DefaultEditorialEvent(Type type,Object title) {
		
		this.title=title.toString();
		this.type = type;
		
		this.user = currentUser().name();
		this.timestamp=time();
	}
	
	protected void subtitle(String title) {
		this.subtitle=title;
	}
	
	@Override
	public String title() {
		return title;
	}

	@Override
	public String subtitle() {
		return subtitle;
	}
	
	public String description() {
		return description;
	}
	
	public void description(String description) {
		this.description = description;
	}
	
	@Override
	public Type type() {
		return type;
	}
	
	@Override
	public String timestamp() {
		return timestamp;
	}
	
	@Override
	public String user() {
		return user;
	}
	
	
	
	@Override
	public int compareTo(EditorialEvent o) {
		return time(o.timestamp()).compareTo(time(timestamp));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		DefaultEditorialEvent other = (DefaultEditorialEvent) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	
}
