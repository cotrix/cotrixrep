package org.cotrix.domain.dsl.grammar;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;

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
	
	public static interface UserChangeClause extends SecondClause, ThirdClause, FourthClause {}

	
	public static interface SecondClause  {
		
		ThirdClause email(String email);
		
		ThirdClause noMail();
	}
	
	
	public static interface ThirdClause extends FourthClause {
		
		
		ThirdClause fullName(String name);
		
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
		
		UserChangeClause isNoLonger(Role ... roles);
		
		UserChangeClause isNoLonger(Collection<Role> roles);
	}
}
