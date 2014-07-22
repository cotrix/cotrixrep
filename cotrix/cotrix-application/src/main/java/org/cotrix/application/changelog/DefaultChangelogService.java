package org.cotrix.application.changelog;

import static java.text.DateFormat.*;
import static org.cotrix.application.managed.ManagedCode.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;

import java.util.Date;

import org.cotrix.application.managed.ManagedCode;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;

public class DefaultChangelogService implements ChangelogService {

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
			log.add(new CodeChange.New(code.id(),getDateTimeInstance().format(created)));
		else
			if (managed.attribute(DELETED)!=null)
				log.add(new CodeChange.Deleted(code.id(),getDateTimeInstance().format(modified)));
			else
				if (modified.after(created))
					log.add(new CodeChange.Modified(code.id(),getDateTimeInstance().format(modified)));
	}
}
