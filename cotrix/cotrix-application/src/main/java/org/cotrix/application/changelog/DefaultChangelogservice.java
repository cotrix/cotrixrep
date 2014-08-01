package org.cotrix.application.changelog;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.application.ChangelogService;
import org.cotrix.domain.codelist.Codelist;

@Singleton
public class DefaultChangelogservice implements ChangelogService {

	@Inject
	private ChangelogManager changelog;
	
	@Override
	public void track(Codelist list) {
		
		changelog.update(list);
	}
}
