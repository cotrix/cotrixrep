package org.cotrix.repository;

import static org.cotrix.domain.attributes.CommonDefinition.*;

import java.util.Date;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.trait.Named;

public class CodelistCoordinates implements Named {

	private final String id;
	private final QName name;
	private final String version;
	private final Date creationDate;
	
	public static CodelistCoordinates coords(String id, QName name,String version, Date creationDate) {
		return new CodelistCoordinates(id, name, version, creationDate);
	}
	
	public static CodelistCoordinates coordsOf(Codelist.Bean list) {
		return coordsOf(list.entity());
	}
	
	public static CodelistCoordinates coordsOf(Codelist list) {
		return new CodelistCoordinates(list.id(), list.qname(), list.version(),CREATED.dateOf(list));
	}
	
	public CodelistCoordinates(String id,QName name, String version, Date creationDate) {
		this.id=id;
		this.name=name;
		this.version=version;
		this.creationDate= creationDate;
	}
	
	public String id() {
		return id;
	}
	
	public Date creationDate() {
		return creationDate;
	}
	
	public QName qname() {
		return name;
	}
	
	public String version() {
		return version;
	}

	@Override
	public String toString() {
		return "CodelistCoordinates [id=" + id + ", name=" + name + ", version=" + version + ", creationDate=" + creationDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
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
