package org.cotrix.domain.user;

import static org.cotrix.action.Action.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Roles.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.po.UserPO;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Versioned;

/**
 * A user of the application.
 * 
 * @author Fabio Simeoni
 *
 */
public interface User extends Identified {

	/**
	 * Returns the name of this user.
	 * @return the name
	 */
	String name();

	/**
	 * Returns the full name of this user.
	 * @return the full name
	 */
	String fullName();
	
	/**
	 * Returns the email of this user.
	 * @return the email
	 */
	String email();
	
	/**
	 * Returns the permissions of this user, including those inherited from roles.
	 * @return the permissions
	 */
	Collection<Action> permissions();
	
	/**
	 * Returns the permissions directly assigned to this user.
	 * @return the declared permissions
	 */
	Collection<Action> directPermissions();
	
	/**
	 * Returns <code>true</code> if this user can perform a given action.
	 * @param action the action
	 * @return <code>true</code> if this user can perform the given action
	 */
	boolean can(Action action);
	
	/**
	 * Returns the roles assigned to this user.
	 * @return the roles
	 */
	Collection<Role> roles();
	
	/**
	 * Returns <code>true<code> if this user has a root role.
	 * 
	 * @return <code>true</code> if this user has a root role
	 */
	boolean isRoot();

	/**
	 * Returns <code>true<code> if this user has a given role.
	 * 
	 * @param role the role
	 * @return <code>true</code> if this user has the given role
	 */
	boolean is(Role role);

	
	/**
	 * Returns the permissions and roles of the user, index by type and by resource.
	 * @return the fingerprint
	 */
	FingerPrint fingerprint();
	
	
	
	/**
	 * A {@link Versioned.Abstract} implementation of {@link Codelist}.
	 * 
	 * @author Fabio Simeoni
	 * 
	 */
	public class Private extends Identified.Abstract<Private> implements User, Serializable {

		private static final long serialVersionUID = 1L;

		private final String name;
		private String fullName;
		private String email;
		private final List<Action> permissions = new ArrayList<Action>();
		private final List<Role> roles = new ArrayList<Role>();

		public Private(UserPO po) {
			super(po);
			this.name = po.name();
			this.fullName = po.fullName();
			this.permissions.addAll(po.permissions());
			this.email = po.email();
			this.roles.addAll(po.roles());
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public String fullName() {
			return fullName;
		}
		
		@Override
		public String email() {
			return email;
		}

		@Override
		public List<Action> permissions() {
			
			ArrayList<Action> permissions = new ArrayList<Action>(directPermissions());

			for (Role role : roles)
				for (Action p : role.permissions())
					if (!permissions.contains(p))
						permissions.add(p);

			return permissions;
		}
		
		@Override
		public Collection<Action> directPermissions() {
			return Collections.unmodifiableCollection(this.permissions);
		}
		
		@Override
		public boolean can(Action action) {
			
			notNull("action", action);
			
			return action.included(permissions());
		}
		
		@Override
		public Collection<Role> roles() {
			
			Collection<Role> roles = new HashSet<Role>();
			
			//compute role closure, recursively
			for (Role r : this.roles) {
				roles.addAll(r.roles());
				roles.add(r);
			}
				
			return roles;
		}
		
		@Override
		public boolean isRoot() {
			return is(ROOT);
		}
		
		@Override
		public boolean is(Role role) {
	
			notNull("role", role);
			
			for (Role myrole : roles)
				if (myrole.equals(role) || myrole.equals(role.on(any)) || myrole.is(role))
						return true;
	
			return false;
		}

		@Override
		public Private copy(boolean withId) {
			
			UserPO po = new UserPO(withId ? id() : null);
			
			po.setName(name);
			po.setFullName(fullName);
			po.setPermissions(permissions);
			po.setRoles(roles);
			
			return new Private(po);
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			if (changeset.fullName() != null && !changeset.fullName().equals(fullName))
				this.fullName = changeset.fullName();
			
			if (changeset.email() != null && !changeset.email().equals(email))
				this.email = changeset.email();

			//replace permissions
			this.permissions.clear();
			this.permissions.addAll(changeset.permissions());
			
			//replace role
			this.roles.clear();
			this.roles.addAll(changeset.roles());
		}
		
		public FingerPrint fingerprint() {
			
			return new FingerPrint(this);
		}
		

		@Override
		public String toString() {
			final int maxLen = 100;
			return "Private [name=" + name + ", fullName=" + fullName + ", permissions="
					+ (permissions != null ? permissions.subList(0, Math.min(permissions.size(), maxLen)) : null)
					+ ", roles=" + (roles != null ? roles.subList(0, Math.min(roles.size(), maxLen)) : null) + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
			result = prime * result + ((roles == null) ? 0 : roles.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Private other = (Private) obj;
			if (fullName == null) {
				if (other.fullName != null)
					return false;
			} else if (!fullName.equals(other.fullName))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (permissions == null) {
				if (other.permissions != null)
					return false;
			} else if (!permissions.equals(other.permissions))
				return false;
			if (roles == null) {
				if (other.roles != null)
					return false;
			} else if (!roles.equals(other.roles))
				return false;
			return true;
		}

		
		

	

		

	}
}
