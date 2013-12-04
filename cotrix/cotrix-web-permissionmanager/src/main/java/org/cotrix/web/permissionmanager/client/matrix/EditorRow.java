/**
 * 
 */
package org.cotrix.web.permissionmanager.client.matrix;

import java.util.HashMap;

import org.cotrix.web.permissionmanager.shared.RoleState;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.share.shared.UIUser;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EditorRow extends RolesRow {
	
	public EditorRow() {
		super(new UIUser("", "", ""), new HashMap<String, RoleState>());
	}
}
