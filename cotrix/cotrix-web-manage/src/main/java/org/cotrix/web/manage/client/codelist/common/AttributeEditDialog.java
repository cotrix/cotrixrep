package org.cotrix.web.manage.client.codelist.common;

import org.cotrix.web.common.shared.Language;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(AttributeEditDialogImpl.class)
public interface AttributeEditDialog {
	
	public interface AttributeEditDialogListener {
		public void onEdit(String name, String type, Language language, String value);
	}
	
	public void set(String name, String type, Language language, String value);

	public void setListener(AttributeEditDialogListener listener);
	
	public void showCentered();
	public void hide();

	public void clean();
}