package org.cotrix.user.dsl;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;
import org.cotrix.domain.Code;
import org.cotrix.user.Role;
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
	
	public static interface UserChangeClause extends SecondClause, ThirdClause, FourthClause {
		
		User delete();
	}

	
	public static interface SecondClause  {
		
		ThirdClause fullName (String string);
	}
	
	
	public static interface ThirdClause {
		
		
		ThirdClause can(Action ... permissions);
		
		ThirdClause can(Collection<Action> permissions);
		
		ThirdClause isRoot();
		
		ThirdClause is(Role ... bindings);
		
		ThirdClause is(Collection<Role> roles);
		
		ThirdClause cannot(Action ... permissions);
		
		User build();
		
		Role buildAsRoleFor(ResourceType type);
	} 
	
	public static interface FourthClause  {
		
		UserChangeClause isNot(Role ... roles);
	}
}
