package org.cotrix.application.changelog;

import static java.lang.String.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.managed.ManagedCodelist.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.cotrix.repository.CodelistQueries.*;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.domain.trait.Status;
import org.cotrix.repository.CodelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Changelog {
	
	private static final Logger log = LoggerFactory.getLogger(Changelog.class);

	@Inject
	private CodelistRepository codelists;
	
	@Inject
	private ChangelogDetector detect;
	
	public void updateWith(Codelist changeset) {
	
		//we know this will succeed, an update has already taken place 
		Codelist changed = codelists.lookup(changeset.id());
		
		//changelog requires lineage
		if (manage(changed).hasno(PREVIOUS_VERSION)) 		
			return;
				
		for (Code change : changeset.codes()) {
		
			Status status = reveal(change).status();
			
			//only if it bring real changes
			if (status==Status.DELETED || manage(change).has(DELETED))
				continue;
			
			Code code = changed.codes().lookup(change.id());
			
			processCode(reveal(code),reveal(change));
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
		
		//changes are wrt to lineage (the codelist has it, but this update is obviously on a new code)
		if (hasno(originId))
			return;
		
		Code origin = codelists.get(code(originId));
		
		if (hasno(origin)) {
			log.error("cannot compute changelog for code {} as its lineage {} can't be retrieved.",change.id(),originId);
			return;
		}
		
		List<ChangelogGroup> changes = detect.changesBetween(origin, changed);
		
		String textualChanges = render(changes);
		
		if (textualChanges.isEmpty()) {
			
			if (has(modified))
				
				attributes.remove(modified.id());

			return;
			
		}

		if (hasno(modified)) {
			
			attributes.add(stateof(attribute().instanceOf(MODIFIED).value("TRUE")));
			modified = managed.attribute(MODIFIED);
		}
		
		stateof(modified).description(textualChanges);
		
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
				
				builder.append(format("├ %s:",group.name()));
				
				for (GroupEntry entry : group.entries())
					builder.append(format("\n\n ✓ %s\n   (%s on %s) ",entry.description(),currentUser().name(), time()));
			}
		

		}
		
		return builder.toString();
		
	}
}
