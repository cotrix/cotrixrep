package org.cotrix.repository.codelist;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.trait.Named;

public class CodelistCoordinates implements Named {

	private final String id;
	private final QName name;
	private final String version;
	
	public static CodelistCoordinates coords(String id, QName name,String version) {
		return new CodelistCoordinates(id, name, version);
	}
	
	public static CodelistCoordinates coordsOf(Codelist list) {
		return new CodelistCoordinates(list.id(), list.name(), list.version());
	}
	
	public CodelistCoordinates(String id,QName name, String version) {
		this.id=id;
		this.name=name;
		this.version=version;
	}
	
	public String id() {
		return id;
	}
	
	public QName name() {
		return name;
	}
	
	public String version() {
		return version;
	}

	@Override
	public String toString() {
		return "[name=" + name + ", version=" + version + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		CodelistCoordinates other = (CodelistCoordinates) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	
	
}
