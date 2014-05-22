/**
 * 
 */
package org.cotrix.web.manage.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistGroup implements IsSerializable {
	
	public static CodelistGroup fromCodelist(UICodelist codelist) {
		CodelistGroup group = new CodelistGroup(codelist.getName());
		group.addVersion(codelist.getId(), codelist.getVersion());
		return group;
	}
	
	private UIQName name;
	private List<Version> versions = new ArrayList<Version>();
	
	public CodelistGroup(){}
	
	/**
	 * @param name
	 */
	public CodelistGroup(UIQName name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public UIQName getName() {
		return name;
	}
	
	public void addVersion(Version version)
	{
		versions.add(version);
		Collections.sort(versions);
	}
	
	public void addVersions(List<Version> versions)
	{
		this.versions.addAll(versions);
		Collections.sort(this.versions);
	}
	
	public void addVersion(String id, String version)
	{
		versions.add(new Version(this, id, version));
		Collections.sort(versions);
	}

	/**
	 * @return the versions
	 */
	public List<Version> getVersions() {
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
		return true;
	}



	public static class Version implements IsSerializable, Comparable<Version> {
		
		protected CodelistGroup parent;
		protected String id;
		protected String version;
		
		protected Version(){}
		
		protected Version(CodelistGroup parent, String id, String version) {
			this.parent = parent;
			this.id = id;
			this.version = version;
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
		 * @return the parent
		 */
		public CodelistGroup getParent() {
			return parent;
		}

		public UICodelist toUICodelist()
		{
			UICodelist codelist = new UICodelist();
			codelist.setId(id);
			codelist.setName(parent.getName());
			codelist.setVersion(version);
			return codelist;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Version [id=");
			builder.append(id);
			builder.append(", version=");
			builder.append(version);
			builder.append("]");
			return builder.toString();
		}

		@Override
		public int compareTo(Version o) {
			return String.CASE_INSENSITIVE_ORDER.compare(version, o.version);
		}
	}
}
