package org.cotrix.repository.user.impl;

import static org.cotrix.repository.user.UserQueries.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.common.Utils;
import org.cotrix.domain.user.User;
import org.cotrix.repository.Repository;
import org.cotrix.repository.impl.DefaultRepository;
import org.cotrix.repository.user.UserRepository;


@ApplicationScoped
public class DefaultUserRepository extends DefaultRepository<User,User.Private,Repository<User.Private>> implements UserRepository {

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
}