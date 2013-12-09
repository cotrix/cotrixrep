package org.cotrix.repository.impl;

import static org.cotrix.repository.UserQueries.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.common.Utils;
import org.cotrix.domain.user.User;
import org.cotrix.repository.Repository;
import org.cotrix.repository.UserRepository;


@ApplicationScoped
public class DefaultUserRepository extends AbstractRepository<User,User.Private,User.State> implements UserRepository {

	@Inject
	public DefaultUserRepository(Repository<User.State> repository) {
		super(repository);
	}
	
	@Override
	public void add(User user) {
		
		Utils.notNull("user",user);
		
		if (get(userByName(user.name()))!=null)
			throw new IllegalStateException("user "+user.name()+" cannot be added as a user with the same name is already in this repository");
		
		super.add(user);
	}
}