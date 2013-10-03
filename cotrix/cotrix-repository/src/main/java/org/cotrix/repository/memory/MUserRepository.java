package org.cotrix.repository.memory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

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
	public MUserRepository() {
		this(new MStore());
	}
	
	@PostConstruct
	public void loadPredefinedUsers() {
		
		log.info("loading predefined users");
		
		for (User user : PredefinedUsers.values)
			this.add(user);
	}
	
	/**
	 * Creates an instance over a given {@link MStore}.
	 * @param store
	 */
	public MUserRepository(MStore store) {
		super(store,User.class,User.Private.class);
	}
	
	@Override
	public void add(User list) {
		
		super.add(list);
		
	}
	
}
