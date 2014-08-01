package org.cotrix.application;

import org.cotrix.domain.codelist.Codelist;


//triggers bulk validation
public interface ChangelogService {

	void track(Codelist list);
}
