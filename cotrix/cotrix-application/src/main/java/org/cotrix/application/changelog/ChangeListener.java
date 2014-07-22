package org.cotrix.application.changelog;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.repository.CodelistQueries.*;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.common.events.Modified;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.domain.trait.Status;
import org.cotrix.repository.CodelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeListener {
	
	private static final Logger log = LoggerFactory.getLogger(ChangeListener.class);

	@Inject
	private ChangeDetector detect;
	
	@Inject
	private CodelistRepository codelists;
	
	public void onChange(@Observes @Modified Codelist changeset){
		
		for (Code change : changeset.codes()) {
			
			if (reveal(change).status()!=Status.MODIFIED)
				continue;
			
			Code changed = codelists.get(code(change.id()));
			
			if (changed==null) {
				log.error("application error: cannot compute changelog for code {} as it can't be retrieved.",change.id());
				continue;
			}
			
			ManagedCode managed = manage(changed);
			
			Attribute modified = managed.attribute(MODIFIED);
			
			if (modified==null) 
				continue;
			
			String id = managed.originId();
			
			if (id==null) {
				log.error("application error: cannot compute changelog for code {} as it has no lineage.",change.id());
				continue;
			}
			
			Code origin = codelists.get(code(id));
			
			if (origin==null) {
				log.error("application error: cannot compute changelog for code {} as its lineage {} can't be retrieved.",change.id(),id);
				continue;
			}
			
			List<CodeChange> changes = detect.changesBetween(origin, change);
			
			reveal(modified).state().description(changes.toString());
			
		
		}
	}
}
