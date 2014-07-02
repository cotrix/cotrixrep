package org.cotrix.application.impl.delegation;

import static org.cotrix.domain.dsl.Users.*;

import javax.enterprise.event.Observes;

import org.cotrix.action.Action;
import org.cotrix.action.events.CodelistActionEvents.Version;
import org.cotrix.common.events.Current;
import org.cotrix.domain.dsl.grammar.UserGrammar.UserChangeClause;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.UserRepository;
import org.jboss.weld.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewVersionListener {

	private static Logger log = LoggerFactory.getLogger(NewVersionListener.class);
	
	
	static void delegateOnVersioning(@Observes Version event, @Current RequestContext ctx, @Current User user, UserRepository users) {
		
		if (!ctx.isActive())
			return;
		
		log.info("delegating {}'s permissions to new version {} of {} ({})", user.name(),event.codelistVersion,event.codelistName,event.codelistId);
		
		UserChangeClause newuser = modifyUser(user);
		
		for (Role role : user.directRoles())
			if (role.resource().equals(event.oldId))
				newuser.is(role.on(event.codelistId));
		
		for (Action permission : user.directPermissions())
			if (permission.resource().equals(event.oldId))
				newuser.can(permission.on(event.codelistId));
		
		User changeset = newuser.build();
		
		users.update(changeset);
	}
}
