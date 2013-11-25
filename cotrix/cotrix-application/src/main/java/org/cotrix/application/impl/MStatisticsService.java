package org.cotrix.application.impl;

import static org.cotrix.repository.CodelistQueries.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.application.StatisticsService;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
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
		
		for (Codelist list : codelistRepository.get(allLists()))
			codes = codes + codelistRepository.get(summary(list.id())).size();
		
		int users = userRepository.size();
		
		int repositories = 0; 
		
		for (@SuppressWarnings("unused") RepositoryService s : cloud.repositories())
			repositories++;
		
		return new Statistics(codelists, codes, users, repositories);
		
	}
}
