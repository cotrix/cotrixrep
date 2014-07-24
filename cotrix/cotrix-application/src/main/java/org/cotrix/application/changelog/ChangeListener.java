package org.cotrix.application.changelog;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.managed.ManagedCodelist.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.cotrix.repository.CodelistQueries.*;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.common.events.Modified;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.CommonDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.NamedStateContainer;
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
		
		attributes.add(stateof(attribute().with(NEW).value("TRUE")));
	}
	
	private void handleModifiedMarkerWith(Code.Private changed,Code.Private change,NamedStateContainer<Attribute.State>  attributes) {
		
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
		
		Map<String,CodeChange> changes = detect.changesBetween(origin, change);
		
		if (changes.isEmpty()) {
			
			if (has(modified))
				attributes.remove(modified.id());

			return;
			
		}

		if (hasno(modified)) {
			
			attributes.add(stateof(attribute().with(MODIFIED).value("TRUE")));
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
		
	private String render(Map<String,CodeChange> changes) {
		
		StringBuilder builder = new StringBuilder();
		
		for (CodeChange change : changes.values())
			builder.append(change.description()).append("\n");
		
		return builder.toString();
	}
}
