package org.cotrix.web.manage.shared;

import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface Group extends IsSerializable {

	/**
	 * TODO move group inside attribute sort info
	 */
	public CodelistEditorSortInfo getSortInfo(boolean ascending);

	public UIQName getName();
	public int getPosition();
	
	public SafeHtml getLabel();

	public String getValue(UICode code);
	
	public Group cloneGroup();
	
	public boolean isEditable();

	public boolean isSystemGroup();

}