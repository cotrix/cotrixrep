package org.cotrix.stage;

import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;

import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.user.User;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.stage.data.SomeUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodelistIngester {

	static private final Logger log = LoggerFactory.getLogger(CodelistIngester.class);
	
	@Inject
	CodelistRepository codelists;

	@Inject
	LifecycleService lifecycle;

	@Inject
	UserRepository users;
	
	void ingest(Codelist list) {

		log.info("ingesting {}", list.name());
		
		codelists.add(list);

		lifecycle.start(list.id());

		for (User user : SomeUsers.owners)
			users.update(modifyUser(user).is(OWNER.on(list.id())).build());
	}
}
