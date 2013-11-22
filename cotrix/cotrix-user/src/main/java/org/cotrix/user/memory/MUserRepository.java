package org.cotrix.user.memory;

import static org.cotrix.user.Users.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.memory.MRepository;
import org.cotrix.repository.memory.MStore;
import org.cotrix.repository.utils.UuidGenerator;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
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
	
	public MUserRepository() {
		this(new UuidGenerator());
	}
	
	@Override
	public User lookupByName(String id) {
		for (User user : getAll())
			if (user.name().equals(id))
				return user;
		
		return null;
						
	}
	
	@Override
	public void add(User user) {
		
		for (User u : getAll())
			if (user.name().equals(u.name()))
				throw new IllegalStateException("user "+user.name()+" cannot be added as a user with the same name already exists");
		
		super.add(user);
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
		
		log.info("loading predefined users "+predefinedUsers);
		
		for (User user : predefinedUsers)
			this.add(user);
	}
	
}
