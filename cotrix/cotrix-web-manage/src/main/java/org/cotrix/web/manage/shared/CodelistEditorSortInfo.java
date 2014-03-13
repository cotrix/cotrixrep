/**
 * 
 */
package org.cotrix.web.manage.shared;

import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class CodelistEditorSortInfo implements IsSerializable {
	
	protected boolean ascending;
	
	protected CodelistEditorSortInfo(){}

	/**
	 * @param ascending
	 */
	public CodelistEditorSortInfo(boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * @return the ascending
	 */
	public boolean isAscending() {
		return ascending;
	}
	
	
	public static class CodeNameSortInfo extends CodelistEditorSortInfo {
		
		CodeNameSortInfo(){}

		public CodeNameSortInfo(boolean ascending) {
			super(ascending);
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("CodeNameSortInfo [ascending=");
			builder.append(ascending);
			builder.append("]");
			return builder.toString();
		}
	}
	
	public static class AttributeGroupSortInfo extends CodelistEditorSortInfo {
		
		protected UIQName name;
		protected UIQName type;
		protected String language;
		protected int position;
		
		AttributeGroupSortInfo(){}
		
		/**
		 * @param ascending
		 * @param name
		 * @param type
		 * @param language
		 */
		public AttributeGroupSortInfo(boolean ascending, UIQName name,
				UIQName type, String language, int position) {
			super(ascending);
			this.name = name;
			this.type = type;
			this.language = language;
		}
		
		/**
		 * @return the name
		 */
		public UIQName getName() {
			return name;
		}
		
		/**
		 * @return the type
		 */
		public UIQName getType() {
			return type;
		}
		
		/**
		 * @return the language
		 */
		public String getLanguage() {
			return language;
		}

		/**
		 * @return the position
		 */
		public int getPosition() {
			return position;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("AttributeGroupSortInfo [name=");
			builder.append(name);
			builder.append(", type=");
			builder.append(type);
			builder.append(", language=");
			builder.append(language);
			builder.append(", position=");
			builder.append(position);
			builder.append(", ascending=");
			builder.append(ascending);
			builder.append("]");
			return builder.toString();
		}
	}
}
