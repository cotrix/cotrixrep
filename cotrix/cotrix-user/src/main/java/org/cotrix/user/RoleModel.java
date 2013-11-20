package org.cotrix.user;

/**
 * A user that serves as a model of permissions to define a role.
 * 
 * @author Fabio Simeoni
 *
 */
public interface RoleModel extends User {

	Role on(String resource);
}
