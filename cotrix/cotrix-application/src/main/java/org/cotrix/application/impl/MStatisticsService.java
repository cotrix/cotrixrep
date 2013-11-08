package org.cotrix.application.impl;

import static org.cotrix.repository.Queries.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.application.StatisticsService;
import org.cotrix.domain.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.user.UserRepository;
import org.virtualrepository.RepositoryService;

@ApplicationScoped
public class MStatisticsService implements StatisticsService {

	@Inject
	private CodelistRepository codelistRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private CloudService cloud;
	
	@Override
	public Statistics statistics() {
	
		int codelists = codelistRepository.size();
		int codes = 0;
		
		for (Codelist list : codelistRepository.queryFor(allLists()))
			codes = codes + codelistRepository.summary(list.id()).size();
		
		int users = userRepository.size();
		
		int repositories = 0; 
		
		for (@SuppressWarnings("unused") RepositoryService s : cloud.repositories())
			repositories++;
		
		return new Statistics(codelists, codes, users, repositories);
		
	}
}