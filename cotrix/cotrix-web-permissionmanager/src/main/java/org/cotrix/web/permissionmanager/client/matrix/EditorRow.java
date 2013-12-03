/**
 * 
 */
package org.cotrix.web.permissionmanager.client.matrix;

import java.util.ArrayList;

import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.UIUser;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EditorRow extends RolesRow {
	
	public EditorRow() {
		super(new UIUser("", ""), new ArrayList<String>());
	}

}
