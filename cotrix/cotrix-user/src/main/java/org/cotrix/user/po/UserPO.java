package org.cotrix.user.po;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.domain.po.EntityPO;

public class UserPO extends EntityPO {

	private final String name;
	private final List<Action> permissions = new ArrayList<Action>();
	
	public UserPO(String id,String name) {
		super(id);
		this.name=name;
	}
	
	public String name() {
		return name;
	}
	
	public void setPermissions(List<Action> permissions) {
		this.permissions.addAll(permissions);
	}
	
	public List<Action> permissions() {
		return permissions;
	}
}
