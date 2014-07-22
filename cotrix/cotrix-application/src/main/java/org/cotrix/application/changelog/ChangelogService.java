package org.cotrix.application.changelog;

import org.cotrix.domain.codelist.Codelist;

public interface ChangelogService {
	
	Changelog changelogFor(Codelist list);
	
}
