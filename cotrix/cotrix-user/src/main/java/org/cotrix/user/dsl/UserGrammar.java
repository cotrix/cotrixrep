package org.cotrix.user.dsl;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.domain.Code;
import org.cotrix.user.Role;
import org.cotrix.user.RoleModel;
import org.cotrix.user.User;

/**
 * The grammar of DSL sentences that create {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class UserGrammar {

	public static interface UserNewClause {
		
		SecondClause name(String name);
		
	}
	
	public static interface UserChangeClause extends SecondClause, ThirdClause {
		
		User delete();
	}

	
	public static interface SecondClause  {
		
		ThirdClause fullName (String string);
	}
	
	public static interface ThirdClause {
		
		
		ThirdClause can(Action ... permissions);
		
		ThirdClause can(Collection<Action> permissions);
		
		ThirdClause is(RoleModel ... models);
		
		ThirdClause is(Role ... bindings);
		
		ThirdClause is(Collection<Role> roles);
		
		ThirdClause cannot(Action ... permissions);
		
		User build();
		
		RoleModel buildAsModel();
	} 
}
