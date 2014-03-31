package org.cotrix.web.manage.client.codelists;

import com.google.inject.ImplementedBy;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(NewCodelistDialogImpl.class)
public interface NewCodelistDialog {


	public interface NewCodelistDialogListener {
		public void onCreate(String name, String version);
	}

	public void setListener(NewCodelistDialogListener listener);

	/** 
	 * {@inheritDoc}
	 */
	public void showCentered();

	/** 
	 * {@inheritDoc}
	 */
	public void clean();

}