package org.cotrix.user.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.user.User;
import org.cotrix.user.po.UserPO;

public class DefaultUser implements User, Serializable {

	private static final long serialVersionUID = 1L;

	private final String id;
	private final String name;
	private final List<Action> permissions = new ArrayList<Action>();
	
	
	public DefaultUser(UserPO po) {
		this.id=po.id();
		this.name=po.name();
		this.permissions.addAll(po.permissions());
	}
	@Override
	public String id() {
		return id;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public List<Action> permissions() {
		return new ArrayList<Action>(permissions);
	}

}
