package org.cotrix.repository.memory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.user.PredefinedUsers;
import org.cotrix.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
@ApplicationScoped
public class MUserRepository extends MRepository<User, User.Private> implements UserRepository {

	private static Logger log = LoggerFactory.getLogger(MUserRepository.class);
	
	/**
	 * Creates an instance over a private {@link MStore}.
	 */
	@Inject
	public MUserRepository(IdGenerator generator) {
		this(new MStore(),generator);
	}

	
	/**
	 * Creates an instance over a given {@link MStore}.
	 * @param store
	 */
	public MUserRepository(MStore store,IdGenerator generator) {
		super(store,User.class,User.Private.class,generator);
	}
	
	@PostConstruct
	public void loadPredefinedUsers() {
		
		log.info("loading predefined users "+PredefinedUsers.values);
		
		for (User user : PredefinedUsers.values)
			this.add(user);
	}

	
	@Override
	public void add(User list) {
		
		super.add(list);
		
	}
	
}
