/**
 * 
 */
package org.cotrix.web.users.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistGroup implements IsSerializable {
	
	protected String name;
	protected List<CodelistVersion> versions = new ArrayList<CodelistVersion>();
	
	public CodelistGroup(){}
	
	/**
	 * @param name
	 * @param versions
	 */
	public CodelistGroup(String name, List<CodelistVersion> versions) {
		this.name = name;
		this.versions = versions;
	}

	/**
	 * @param name
	 */
	public CodelistGroup(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public void addVersion(CodelistVersion version)
	{
		versions.add(version);
		Collections.sort(versions);
	}
	
	public void addVersions(List<CodelistVersion> versions)
	{
		this.versions.addAll(versions);
		Collections.sort(this.versions);
	}
	
	public void addVersion(String id, String version, List<String> roles)
	{
		versions.add(new CodelistVersion(this.name, id, version, roles));
		Collections.sort(versions);
	}

	/**
	 * @return the versions
	 */
	public List<CodelistVersion> getVersions() {
		return versions;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodeListGroup [name=");
		builder.append(name);
		builder.append(", versions=");
		builder.append(versions);
		builder.append("]");
		return builder.toString();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((versions == null) ? 0 : versions.hashCode());
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodelistGroup other = (CodelistGroup) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (versions == null) {
			if (other.versions != null)
				return false;
		} else if (!versions.equals(other.versions))
			return false;
		return true;
	}



	public static class CodelistVersion implements IsSerializable, Comparable<CodelistVersion> {
		
		protected String name;
		protected String id;
		protected String version;
		protected List<String> roles;
		
		protected CodelistVersion(){}
		
		public CodelistVersion(String name, String id, String version, List<String> roles) {
			this.name = name;
			this.id = id;
			this.version = version;
			this.roles = roles;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}
		
		/**
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the roles
		 */
		public List<String> getRoles() {
			return roles;
		}
		

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((roles == null) ? 0 : roles.hashCode());
			result = prime * result
					+ ((version == null) ? 0 : version.hashCode());
			return result;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CodelistVersion other = (CodelistVersion) obj;
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
			if (roles == null) {
				if (other.roles != null)
					return false;
			} else if (!roles.equals(other.roles))
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Version [name=");
			builder.append(name);
			builder.append(", id=");
			builder.append(id);
			builder.append(", version=");
			builder.append(version);
			builder.append(", roles=");
			builder.append(roles);
			builder.append("]");
			return builder.toString();
		}

		@Override
		public int compareTo(CodelistVersion o) {
			return String.CASE_INSENSITIVE_ORDER.compare(version, o.version);
		}
	}
}
