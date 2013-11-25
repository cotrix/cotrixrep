package org.cotrix.user.impl;

import static org.cotrix.user.Users.*;
import static org.cotrix.user.queries.UserQueries.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.common.Utils;
import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.repository.Repository;
import org.cotrix.repository.impl.DefaultRepository;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class DefaultUserRepository extends DefaultRepository<User,User.Private,Repository<User.Private>> implements UserRepository {

	private static Logger log = LoggerFactory.getLogger(UserRepository.class);

	@Inject
	public DefaultUserRepository(Repository<User.Private> repository) {
		super(repository,User.class,User.Private.class);
	}
	
	@Override
	public void add(User user) {
		
		Utils.notNull("user",user);
		
		if (get(userByName(user.name()))!=null)
			throw new IllegalStateException("user "+user.name()+" cannot be added as a user with the same name is already in this repository");
		
		super.add(user);
	}
	
	
	@PostConstruct
	public void loadPredefinedUsers() {
		
		log.info("loading predefined users ");
		
		for (User user : predefinedUsers())
			this.add(user);
	}
}