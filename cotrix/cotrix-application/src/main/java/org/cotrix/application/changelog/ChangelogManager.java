package org.cotrix.application.changelog;

import static java.lang.System.*;
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
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.LinkDefinition;
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

	private static Type logtype = new TypeToken<List<ChangelogEntry>>() {
	}.getType();

	@Inject
	private CodelistRepository codelists;

	public void updateWith(Codelist changeset) {
	
		//we know this will succeed, an update has already taken place 
		Codelist list = codelists.lookup(changeset.id());
		
		//changelog requires lineage
		if (manage(list).hasno(PREVIOUS_VERSION)) 		
			return;
		
		Codelist.Private plist = reveal(list);
		Codelist.Private pchangeset = reveal(changeset);
		
		if (isBulkUpdate(pchangeset))
		
			processBulk(plist);
		
		else //exclusive because bulk-handling currently processes all codes
			
			processPunctual(plist, pchangeset);
	}
	
	public void update(String id) {
	
		//we know this will succeed, an update has already taken place 
		Codelist list = codelists.lookup(id);
		
		processBulk(reveal(list));
	}
	
	private void processBulk(Codelist.Private list) {
		
		//this seems coarse: any bulk change triggers a full traversal
		//in practice we do expect shared defs to occur in most-to-all codes.
		//even if the impacted codes were a few, how much overhead would we be adding to the 
		//traversal required underneatah to find the impact right subset of codes? 
		//would need to measure. surely, fine-grain requires more work and we should not
				
		for (Code.Private changed : list.codes()) 
			handleModifiedMarkerWith(changed);
	}

	private void processPunctual(Codelist.Private list, Codelist.Private changeset) {

		long time = currentTimeMillis();
		
		for (Code.Private change : changeset.codes()) {

			Status status = reveal(change).status();

			// changelog is pointless if the code is to be removed anyway
			if (status == Status.DELETED)
				continue;

			Code.Private code = list.codes().lookup(change.id());

			processCode(code,change);
		}
		
		log.trace("computed full changelog codelist {} in {} msec.",list.id(),currentTimeMillis()-time);
	}

	private void processCode(Code.Private changed, Code.Private change) {

		if (change.status() == null)

			aaddNewMarkerTo(changed.state().attributes());

		else

			handleModifiedMarkerWith(changed);

	}

	private void aaddNewMarkerTo(NamedStateContainer<Attribute.State> attributes) {

		attributes.add(stateof(attribute().instanceOf(NEW).value("TRUE")));
	}

	private void handleModifiedMarkerWith(Code.Private changed) {

		NamedStateContainer<Attribute.State> attributes = changed.state().attributes();

		ManagedCode managed = manage(changed);

		Attribute modified = managed.attribute(MODIFIED);

		String originId = managed.originId();

		// by now we know codelist has lineage, but this update may be for a new
		// code
		if (notmarked(originId))
			return;

		// fetch the past
		Code origin = codelists.get(code(originId));

		if (notmarked(origin)) {
			log.error("cannot compute changelog for code {} as its lineage {} can't be retrieved.", changed.id(), originId);
			return;
		}

		ChangelogProducer producer = new ChangelogProducer();

		Changelog log = producer.changesBetween(origin, changed);

		if (log.isEmpty()) {

			if (marked(modified))

				attributes.remove(modified.id());

			return;

		}

		if (marked(modified)) {

			String oldlog = modified.value();

			List<ChangelogEntry> oldentries = jsonBinder().fromJson(oldlog, logtype);

			log.addAll(oldentries);

		} else {

			attributes.add(stateof(attribute().instanceOf(MODIFIED)));
			modified = managed.attribute(MODIFIED); // remember persistence

		}

		List<ChangelogEntry> entries = log.entries();

		sort(entries);

		stateof(modified).value(jsonBinder().toJson(entries, logtype));

	}

	// helper
	private boolean isBulkUpdate(Codelist.Private changeset) {

		// at least one change to definitions, other than additions

		for (AttributeDefinition.Private def : changeset.definitions())
			if (def.status() != null)
				return true;

		for (LinkDefinition.Private def : changeset.links())
			if (def.status() != null)
				return true;

		return false;
	}

	private boolean marked(Object o) {
		return o != null;
	}

	private boolean notmarked(Object o) {
		return o == null;
	}
}
