package org.cotrix.user.po;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.domain.po.EntityPO;

public class UserPO extends EntityPO {

	private String name;
	private final List<Action> permissions = new ArrayList<Action>();
	
	public UserPO(String id) {
		super(id);
	}
	
	public void setName(String name) {
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
