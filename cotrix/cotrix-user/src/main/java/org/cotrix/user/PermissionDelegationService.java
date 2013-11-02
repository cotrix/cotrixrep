package org.cotrix.user;

import java.util.Collection;

import org.cotrix.action.Action;


public interface PermissionDelegationService {

	/**
	 * Delegates one or more permissions from the current user to a given user.
	 * @param permissions the permissions
	 * @return the clause for the selection of the target user
	 */
	DelegatePermissionClause delegate(Action ... permissions);

	/**
	 * Delegates one or more permissions from the current user to a given user.
	 * @param permissions the permissions
	 * @return the clause for the selection of the target user
	 */
	DelegatePermissionClause delegate(Collection<Action> permissions);
	
	/**
	 * Revokes one or more permissions from a given user.
	 * @param permissions the permissions
	 * @return the clause for the selection of the target user
	 */
	RevokePermissionClause revoke(Action ... permissions);

	/**
	 * Revokes one or more permissions from a given user.
	 * @param permissions the permissions
	 * @return the clause for the selection of the target user
	 */
	RevokePermissionClause revoke(Collection<Action> permissions);
	
	static interface DelegatePermissionClause {
		
		/**
		 * Selects the target user.
		 * @param user the user
		 * 
		 * @throws IllegalAccessError if the delegation is not allowed by the current policy
		 */
		void to(User user);
	}
	
	static interface RevokePermissionClause {
		
		/**
		 * Selects the target user.
		 * @param user the user
		 * 
		 * @throws IllegalAccessError if the delegation is not allowed by the current policy
		 * @throws IllegalStateException if the target user does not have one of the permissions
		 */
		void from(User u);
	}
}
