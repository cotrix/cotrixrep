package org.cotrix.application.changelog;

import static java.lang.String.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.managed.ManagedCodelist.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.cotrix.repository.CodelistQueries.*;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.common.events.Modified;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.domain.trait.Status;
import org.cotrix.repository.CodelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangelogManager {
	
	private static final Logger log = LoggerFactory.getLogger(ChangelogManager.class);

	@Inject
	private ChangelogDetector detect;
	
	@Inject
	private CodelistRepository codelists;
	
	
	//entry point for incremental computations
	public void onCodelistUpdate(@Observes @Modified Codelist changeset){
		
		Codelist list = codelists.lookup(changeset.id());
		
		//changelog requires lineage
		if (manage(list).hasno(PREVIOUS_VERSION)) 		
			return;
		
		log.trace("updating changelog for codelist {}",changeset.id());
		
		for (Code change : changeset.codes()) {
		
			Status status = reveal(change).status();
			
			//only if it bring real changes
			if (status==Status.DELETED || manage(change).has(DELETED))
				continue;
			
			Code changed = list.codes().lookup(change.id());
			
			processCode(reveal(changed),reveal(change));
		}
		
				
	}
	
	
	private void processCode(Code.Private changed, Code.Private change) {
		
		NamedStateContainer<Attribute.State>  attributes = changed.state().attributes();
		
		if (change.status()==null)
			
			aaddNewMarkerTo(attributes);
		
		else 
			
			handleModifiedMarkerWith(changed, change, attributes);
		
		
	}
	
	private void aaddNewMarkerTo(NamedStateContainer<Attribute.State>  attributes) {
		
		attributes.add(stateof(attribute().instanceOf(NEW).value("TRUE")));
	}
	
	private void handleModifiedMarkerWith(Code.Private changed, Code.Private change, NamedStateContainer<Attribute.State>  attributes) {
		
		ManagedCode managed = manage(changed);
		Attribute modified = managed.attribute(MODIFIED);

		String originId = managed.originId();
		
		//changes are wrt to lineage (the codelist has it, but this code is new)
		if (hasno(originId))
			return;
		
		Code origin = codelists.get(code(originId));
		
		if (hasno(origin)) {
			log.error("application error: cannot compute changelog for code {} as its lineage {} can't be retrieved.",change.id(),originId);
			return;
		}
		
		List<ChangelogGroup> changes = detect.changesBetween(origin, changed);
		
		if (changes.isEmpty()) {
			
			if (has(modified))
				attributes.remove(modified.id());

			return;
			
		}

		if (hasno(modified)) {
			
			attributes.add(stateof(attribute().instanceOf(MODIFIED).value("TRUE")));
			modified = managed.attribute(MODIFIED);
		}
		
		stateof(modified).description(render(changes));
		
	}

	
	//helper
	private boolean has(Object o) {
		return o!=null;
	}
	
	private boolean hasno(Object o) {
		return o==null;
	}
		
	private String render(List<ChangelogGroup> changes) {
		
		StringBuilder builder = new StringBuilder();
		
		for (ChangelogGroup group : changes) {
			
			if (group.entries().isEmpty())
				continue;
			else {
			
				if (builder.length()>0)
					builder.append("\n\n");
				
				builder.append(group.name()).append(":");
				
				for (GroupEntry entry : group.entries())
					builder.append(format("\n ・ %s\n   (%s on %s) ",entry.description(),currentUser().name(), time()));
			}
		

		}
		
		return builder.toString();
		
	}
}
