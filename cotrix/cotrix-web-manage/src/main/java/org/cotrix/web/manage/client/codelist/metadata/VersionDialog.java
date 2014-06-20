package org.cotrix.web.manage.client.codelist.metadata;

import com.google.inject.ImplementedBy;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(VersionDialogImpl.class)
public interface VersionDialog {
	
	
	public interface VersionDialogListener {
		public void onCreate(String id, String newVersion);
	}

	public void setListener(VersionDialogListener listener);

	public void showCentered();

	public void setOldVersion(String id, String name, String oldVersion);

}