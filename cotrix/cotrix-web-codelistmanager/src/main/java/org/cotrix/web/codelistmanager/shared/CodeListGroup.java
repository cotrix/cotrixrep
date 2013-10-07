/**
 * 
 */
package org.cotrix.web.codelistmanager.shared;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListGroup implements IsSerializable {
	
	protected String name;
	protected List<Version> versions = new ArrayList<Version>();
	
	public CodeListGroup(){}
	
	/**
	 * @param name
	 */
	public CodeListGroup(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public void addVersion(String id, String version)
	{
		versions.add(new Version(this, id, version));
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
	
	public static class Version implements IsSerializable, Comparable<Version> {
		
		protected CodeListGroup parent;
		protected String id;
		protected String version;
		
		protected Version(){}
		
		protected Version(CodeListGroup parent, String id, String version) {
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
