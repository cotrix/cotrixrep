package org.cotrix.domain.versions;



public class SimpleVersion implements Version {
	
	private final String value;
	
	public SimpleVersion() {
		this("0");
	}
	
	public SimpleVersion(String value) {
		this.value=value;
	}
	
	@Override
	public String value() {
		return value;
	}

	@Override
	public SimpleVersion bumpTo(String version) {
		if (value.compareToIgnoreCase(version)<0)
			return new SimpleVersion(version);
		else
			throw new IllegalStateException("cannot bump version "+value+" to "+version);
	}
	
	public Version copy() {
		return new SimpleVersion(value);
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
		SimpleVersion other = (SimpleVersion) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
	
	

}