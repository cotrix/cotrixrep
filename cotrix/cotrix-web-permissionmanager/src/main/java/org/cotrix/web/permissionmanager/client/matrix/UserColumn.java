/**
 * 
 */
package org.cotrix.web.permissionmanager.client.matrix;

import org.cotrix.web.permissionmanager.shared.RolesRow;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserColumn extends Column<RolesRow, String> {
	
	protected UserCell userCell;
	
	public UserColumn(SuggestOracle oracle) {
		this(new UserCell(oracle));
	}
		
	public UserColumn(UserCell cell) {
		super(cell);
		this.userCell = cell;
	}

	@Override
	public String getValue(RolesRow row) {
		return (row instanceof EditorRow)?"":row.getUser()!=null?row.getUser().getFullName():null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onBrowserEvent(Context context, Element elem, RolesRow object,
			NativeEvent event) {
		userCell.setRenderAsInput(object instanceof EditorRow);
		super.onBrowserEvent(context, elem, object, event);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void render(Context context, RolesRow object, SafeHtmlBuilder sb) {
		userCell.setRenderAsInput(object instanceof EditorRow);
		super.render(context, object, sb);
	}

}
