package org.cotrix.application.changelog;

import static org.cotrix.application.managed.ManagedCode.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.application.managed.ManagedCode;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;

@Singleton
public class DefaultChangelogService implements ChangelogService {
	
	@Inject
	private CodelistRepository codelists;
	
	@Inject
	private ChangeDetector detector;	

	@Override
	public Changelog changelogFor(Codelist list) {
		
		Changelog.Private log =  new DefaultChangelog(list);
		
		try {
			
			for (Code code : list.codes())
				process(log,code);	
			
		}
		catch(Exception e) {
			rethrow("cannot generate changelog for "+list.id()+" (see cause)", e);
		}
		
		
		return log;
	}

	
	private void process(Changelog.Private log, Code code) {
		
		ManagedCode managed = manage(code);
		
		String origin = managed.originId();
		Date created = managed.created();
		Date modified = managed.lastUpdated();
		
		if (origin==null)
			log.add(new CodelistChange.NewCode(managed));
		else
			if (managed.attribute(DELETED)!=null)
				log.add(new CodelistChange.DeletedCode(managed));
			else
				if (modified.after(created)) {
				
					List<CodeChange> changes =  detector.changesBetween(find(origin),managed);
					
					log.add(new CodelistChange.ModifiedCode(managed,changes));
					
				}
	}
	
	private Code find(String id) {
		return null;
	}
}
