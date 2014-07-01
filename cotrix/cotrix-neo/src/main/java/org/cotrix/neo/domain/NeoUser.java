package org.cotrix.neo.domain;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.domain.user.User.Private;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;

public class NeoUser extends NeoIdentified implements User.State {

	public static final NeoStateFactory<User.State> factory = new NeoStateFactory<User.State>() {
		
		@Override
		public User.State beanFrom(Node node) {
			return new NeoUser(node);
		}
		
		@Override
		public Node nodeFrom(User.State state) {
			return new NeoUser(state).node();
		}
	};
	
	
	public NeoUser(Node node) {
		super(node);
	}
	
	
	public NeoUser(User.State state) {

		super(USER,state);	
		

		name(state.name());
		fullName(state.fullName());
		email(state.email());
		permissions(state.permissions());
		roles(state.roles());
		
	}


	@Override
	public Private entity() {
		return new User.Private(this);
	}


	@Override
	public String name() {
		return (String) node().getProperty(name_prop);
	}


	@Override
	public void name(String name) {
		node().setProperty(name_prop,name.toString());		
	}


	@Override
	public String fullName() {
		return (String) node().getProperty(fullname_prop);
	}


	@Override
	public void fullName(String name) {
		node().setProperty(fullname_prop,name.toString());
	}


	@Override
	public String email() {
		return (String) node().getProperty(email_prop);
	}


	@Override
	public void email(String name) {
		node().setProperty(email_prop,name.toString());	
		
	}


	@Override
	@SuppressWarnings("all")
	public Collection<Action> permissions() {
		return (Collection<Action>) binder().fromXML((String) node().getProperty(permissions_prop));
	}


	@Override
	public void permissions(Collection<Action> permissions) {
		node().setProperty(permissions_prop,binder().toXML(permissions));	
	}

	

	@Override
	@SuppressWarnings("all")
	public Collection<Role> roles() {
		return (Collection<Role>) binder().fromXML((String) node().getProperty(roles_prop));
	}


	@Override
	public void roles(Collection<Role> roles) {
		node().setProperty(roles_prop,binder().toXML(roles));	
	}
	
	

}
