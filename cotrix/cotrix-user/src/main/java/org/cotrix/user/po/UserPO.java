package org.cotrix.user.po;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.domain.po.DomainPO;

public class UserPO extends DomainPO {

	private String userName;
	private String fullName;
	
	private final List<Action> permissions = new ArrayList<Action>();
	
	public UserPO(String id) {
		super(id);
	}
	
	public void setFullName(String name) {
		valid("username",name);
		this.fullName=name;
	}
	
	public void setName(String name) {
		valid("full name",name);
		this.userName=name;
	}
	
	public String fullName() {
		return fullName;
	}
	
	public String name() {
		return userName;
	}
	
	public void setPermissions(List<Action> permissions) {
		this.permissions.addAll(permissions);
	}
	
	public List<Action> permissions() {
		return permissions;
	}
}
