package org.cotrix.user.impl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.memory.MRepository;
import org.cotrix.repository.memory.MStore;
import org.cotrix.user.PredefinedUsers;
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

	@Inject @Current
	User currentUser;
	
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
	public void add(User user) {
		
		super.add(user);
		
	}
	
	@Override
	public void update(User changeset) {
		
			for (Action action : changeset.permissions())
				if (currentUser!=PredefinedUsers.cotrix && action.isTemplate())
					throw new IllegalAccessError(currentUser.name()+" cannot delegate template "+action+", as it does not have root privileges");
				else
					if (!action.included(currentUser.permissions()))
						throw new IllegalAccessError(currentUser.name()+" cannot perform "+action+", hence cannot add|remove it for "+changeset.id());
			
		super.update(changeset);
	}
	
}
