package org.cotrix.application.changelog;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.repository.CodelistQueries.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.managed.ManagedCode;
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
			
			Map<String,ManagedCode> originsIds = new HashMap<>();
			
			for (Code code : list.codes()) {
			
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
							if (modified.after(created))
								originsIds.put(origin, managed);
				
				}

			
			if (!originsIds.isEmpty()) {
				
			
				//retrieve origins
				Map<String,Code> origins = find(list,originsIds.keySet());
					
				//compute changesets
				
				for (Entry<String,ManagedCode> e : originsIds.entrySet()) {
					
					ManagedCode newcode = e.getValue();
					
					Code oldcode = origins.get(e.getKey());
					
					List<CodeChange> changes =  detector.changesBetween(oldcode,newcode);
					
					log.add(new CodelistChange.ModifiedCode(newcode,changes));
				}
			
			}
		}
		catch(Exception e) {
			rethrow("cannot generate changelog for "+list.id()+" (see cause)", e);
		}
		
		
		return log;
	}
	
	private Map<String,Code> find(Codelist list, Set<String> ids) {
		
		Map<String,Code> origins = new HashMap<>();
		
		for (Code code : codelists.get(codes(ids)))
			origins.put(code.id(),code);
		
		return origins;
	}
}
