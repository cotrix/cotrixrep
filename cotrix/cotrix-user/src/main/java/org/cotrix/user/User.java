package org.cotrix.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cotrix.action.Action;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Identified;
import org.cotrix.user.po.UserPO;

public interface User extends Identified {

	
	String name();
	
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
		private final List<Action> permissions = new ArrayList<Action>();
		
		
		public Private(UserPO po) {
			super(po);
			this.name=po.name();
			this.permissions.addAll(po.permissions());
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public List<Action> permissions() {
			return new ArrayList<Action>(permissions);
		}
		
		@Override
		public Private copy(IdGenerator generator) {
			UserPO po = new UserPO(generator.generateId(), name);
			po.setPermissions(permissions);
			return new Private(po);
		}
		
		@Override
		public String toString() {
			return name();
		}
	}
}
