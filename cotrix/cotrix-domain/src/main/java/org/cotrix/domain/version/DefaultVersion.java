package org.cotrix.domain.version;

import org.cotrix.common.Utils;



public class DefaultVersion implements Version {
	
	private final String value;
	
	public DefaultVersion() {
		this("1.0");
	}
	
	public DefaultVersion(String value) {
		
		Utils.notEmpty("version",value);
		
		this.value=value;
	}
	
	@Override
	public String value() {
		return value;
	}

	@Override
	public DefaultVersion bumpTo(String version) {
		if (value.compareToIgnoreCase(version)<0)
			return new DefaultVersion(version);
		else
			throw new IllegalStateException("cannot bump version "+value+" to "+version);
	}
	
	public Version copy() {
		return new DefaultVersion(value);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		DefaultVersion other = (DefaultVersion) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
	
	

}
