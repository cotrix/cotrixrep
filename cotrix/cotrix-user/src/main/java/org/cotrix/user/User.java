package org.cotrix.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.user.po.UserPO;

public interface User extends Identified {

	String name();

	String fullName();

	Collection<Action> permissions();

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
		private final List<Action> permissions = new ArrayList<Action>();

		public Private(UserPO po) {
			super(po);
			this.name = po.name();
			this.fullName = po.fullName();
			this.permissions.addAll(po.permissions());
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
		public List<Action> permissions() {
			return new ArrayList<Action>(permissions);
		}

		@Override
		public Private copy(boolean withId) {
			UserPO po = new UserPO(withId ? name : null);
			po.setPermissions(permissions);
			return new Private(po);
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			if (changeset.fullName() != null && !changeset.fullName().equals(fullName))
				this.fullName = changeset.fullName();

			//replace permissions
			this.permissions.clear();
			this.permissions.addAll(changeset.permissions());
		}

		@Override
		public String toString() {
			final int maxLen = 100;
			return "Private [name=" + name + ", fullName=" + fullName + ", permissions="
					+ (permissions != null ? permissions.subList(0, Math.min(permissions.size(), maxLen)) : null) + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
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
			return true;
		}

	}
}
