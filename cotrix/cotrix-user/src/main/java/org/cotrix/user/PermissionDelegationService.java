package org.cotrix.user;

import java.util.Collection;

import org.cotrix.action.Action;


public interface PermissionDelegationService {

	/**
	 * Delegates one or more permissions from the current user to a given user.
	 * @param permissions the permissions
	 * @return the clause for the selection of the target user
	 * 
	 */
	DelegateClause delegate(Action ... permissions);

	/**
	 * Delegates one or more permissions from the current user to a given user.
	 * @param permissions the permissions
	 * @return the clause for the selection of the target user
	 */
	DelegateClause delegate(Collection<Action> permissions);
	
	
	/**
	 * Delegates one or more roles from the current user to a given user.
	 * @param roles the roles
	 * @return the clause for the selection of the target user
	 */
	DelegateClause delegate(Role ... roles);
	
	/**
	 * Revokes one or more permissions from a given user.
	 * @param permissions the permissions
	 * @return the clause for the selection of the target user
	 */
	RevokeClause revoke(Action ... permissions);
	
	
	/**
	 * Revokes one or more roles from a given user.
	 * @param roles the roles
	 * @return the clause for the selection of the target user
	 */
	RevokeClause revoke(Role ... roles);

	/**
	 * Revokes one or more permissions from a given user.
	 * @param permissions the permissions
	 * @return the clause for the selection of the target user
	 */
	RevokeClause revoke(Collection<Action> permissions);
	
	
	static interface DelegateClause {
		
		/**
		 * Selects the target user for delegation.
		 * @param user the user
		 * 
		 * 
		 * @throws IllegalArgumentException if the user has no identifier
		 * @throws IllegalAccessError if the delegation is not allowed by the current policy
		 * */
		void to(User user);
	}
	
	static interface RevokeClause {
		
		/**
		 * Selects the target user.
		 * @param user the user
		 * 
		 * @throws IllegalArgumentException if the user has no identifier
		 * @throws IllegalAccessError if the revocation is not allowed by the current policy
		 */
		void from(User u);
	}
}
