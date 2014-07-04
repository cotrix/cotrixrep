/**
 * 
 */
package org.cotrix.web.users.server.util;

import java.io.Serializable;
import java.util.Comparator;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.cotrix.common.BeanSession;
import org.cotrix.common.events.Current;
import org.cotrix.domain.user.User;
import org.cotrix.web.users.shared.RolesRow;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SessionScoped
public class RolesSorter implements Comparator<RolesRow>, Serializable {
	
	private static final long serialVersionUID = 7240964574176390292L;
	
	@Inject @Current
	private BeanSession session;
	
	protected User currentUser;
	
	public void syncUser() {
		currentUser = session.get(User.class);
	}

	@Override
	public int compare(RolesRow row1, RolesRow row2) {
		if (row1.getUser().getId().equals(currentUser.id())) return -1;
		if (row2.getUser().getId().equals(currentUser.id())) return 1;
		return 0;
	}

}
