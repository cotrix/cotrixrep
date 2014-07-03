package org.cotrix.repository.impl;

import static org.cotrix.repository.UserQueries.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.common.CommonUtils;
import org.cotrix.domain.user.User;
import org.cotrix.repository.UserRepository;
import org.cotrix.repository.spi.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class BaseUserRepository extends AbstractRepository<User,User.Private,User.State> implements UserRepository {

	//logs 'on behalf' of interface, for clarity
	private static Logger log = LoggerFactory.getLogger(UserRepository.class);

	@Inject
	public BaseUserRepository(StateRepository<User.State> repository, EventProducer producer) {
		super(repository,producer);
	}
	
	@Override
	public void add(User user) {
		
		CommonUtils.notNull("user",user);
		
		if (get(userByName(user.name()))!=null)
			throw new IllegalStateException("user "+user.name()+" cannot be added as a user with the same name is already in this repository");
		
		log.trace("added user: {}",user.name());
		
		
		super.add(user);
	}
}