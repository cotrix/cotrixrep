package org.cotrix.application.changelog;

import static java.util.Collections.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.managed.ManagedCodelist.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.cotrix.repository.CodelistQueries.*;

import java.lang.reflect.Type;
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

import com.google.gson.reflect.TypeToken;

@Singleton
public class ChangelogManager {
	
	private static final Logger log = LoggerFactory.getLogger(ChangelogManager.class);

	@Inject
	private CodelistRepository codelists;
	
	
	public void updateWith(Codelist changeset) {
	
		//we know this will succeed, an update has already taken place 
		Codelist changed = codelists.lookup(changeset.id());
		
		//changelog requires lineage
		if (manage(changed).hasno(PREVIOUS_VERSION)) 		
			return;
				
		for (Code change : changeset.codes()) {
		
			Status status = reveal(change).status();
			
			//changelog is pointless if the code is to be removed anyway
			if (status==Status.DELETED)
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
		
		//by now we know codelist has lineage, but this update may be for a new code
		if (hasno(originId))
			return;
		
		//fetch the past
		Code origin = codelists.get(code(originId));
		
		if (hasno(origin)) {
			log.error("cannot compute changelog for code {} as its lineage {} can't be retrieved.",change.id(),originId);
			return;
		}
		
		ChangelogProducer producer = new ChangelogProducer();
		
		Changelog log = producer.changesBetween(origin, changed);
		
		if (log.entries().isEmpty()) {
			
			if (has(modified))
				
				attributes.remove(modified.id());

			return;
			
		}
		
		Type entrytype = new TypeToken<List<ChangelogEntry>>(){}.getType();
		
		if (hasno(modified)) {
			
			attributes.add(stateof(attribute().instanceOf(MODIFIED)));
			modified = managed.attribute(MODIFIED);
		}
		else {
			
			List<ChangelogEntry> existingEntries = jsonBinder().fromJson(modified.value(),entrytype) ;
			
			log.addAll(existingEntries);
			
			
		}
		

		List<ChangelogEntry> entries = log.entries();

		sort(entries);
			
		stateof(modified).value(jsonBinder().toJson(entries,entrytype));
		
	}
	

	
	//helper
	private boolean has(Object o) {
		return o!=null;
	}
	
	private boolean hasno(Object o) {
		return o==null;
	}
}
