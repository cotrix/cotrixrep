package org.cotrix.application.changelog;

import static java.lang.Math.*;
import static java.lang.System.*;
import static java.util.Collections.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.cotrix.repository.CodelistQueries.*;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.application.ChangelogService;
import org.cotrix.common.async.CancelledTaskException;
import org.cotrix.common.async.TaskContext;
import org.cotrix.common.async.TaskUpdate;
import org.cotrix.domain.attributes.Attributes;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.Status;
import org.cotrix.repository.CodelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;

@Singleton
public class DefaultChangelogService implements ChangelogService {

	
	private static final Logger log = LoggerFactory.getLogger(DefaultChangelogService.class);

	private static Type logtype = new TypeToken<List<ChangelogEntry>>() {}.getType();

	@Inject
	private CodelistRepository codelists;

	
	@Override
	public void track(Codelist list, boolean optimised) {
		
		TaskContext context = new TaskContext();

		float progress=0f;
		
		Container<? extends Code> codes =  list.codes();
		
		int total = list.codes().size();
		
		//arbitrary
		long step = round(max(10,floor(codes.size()/10)));
		
		int i=0;
		
		long time = currentTimeMillis();
		
		Date listCreated = CREATED.dateOf(list);
		
		for (Code code : codes) {
		
			i++;
			progress++;
			
			Date codeUpdated = LAST_UPDATED.dateOf(code);
			
			//process only if it has changed since this list's version was created
			
			if (!optimised || (codeUpdated!=null && optimised && codeUpdated.after(listCreated)))
			
				handleModifiedMarkerWith(code);
			
			
			if (i%step==0)
				
				if (context.isCancelled()) {
					log.info("cahngelog tracking aborted on user request after {} codes.",i);
					throw new CancelledTaskException("changelog tracking aborted on user request");
				}
			
				else
				
					context.save(TaskUpdate.update(progress/total, "tracked "+i+" codes"));
				
		}
		

		log.trace("tracked changelog for {} in {} msec.",signatureOf(list),currentTimeMillis()-time);
	}
	

	public void trackAfter(Codelist.Private changeset) {
		
		//we know this will succeed, an update has already taken place 
		Codelist list = codelists.lookup(changeset.id());
		
		trackPunctual(reveal(list), changeset);
				
	}
	
	private void trackPunctual(Codelist list, Codelist.Private changeset) {

		boolean isVersion =  PREVIOUS_VERSION.isIn(list);
		
		for (Code.Private change : changeset.codes()) {

			// changelog is pointless if the code is to be removed anyway
			if (change.status() == Status.DELETED)
				continue;
			
			Code code = list.codes().lookup(change.id());
			
			trackCode(code,change,isVersion);
		}
		
	}

	private void trackCode(Code changed, Code.Private change, boolean isVersion) {

		if (change.isChangeset()) {

			LAST_UPDATED.set(time()).on(changed);
		
			if (isVersion)
				handleModifiedMarkerWith(changed);

		}
		else {

			CREATED.set(time()).on(changed);

			if (isVersion)
				NEW.set().on(changed);
	    }
			
		UPDATED_BY.set(currentUser().name()).on(changed);
		
	}
	
	private void handleModifiedMarkerWith(Code changed) {
		
		Attributes attributes = changed.attributes();

		String originId = PREVIOUS_VERSION_ID.in(attributes);

		// by now we know codelist has lineage, but this update may be for a new
		// code
		if (originId==null)
			return;
		
		
		// fetch the past
		Code origin = codelists.get(code(originId));

		if (origin==null) {
			log.error("cannot compute changelog for code {} as its lineage {} can't be retrieved.", changed.id(), originId);
			return;
		}

		ChangelogProducer producer = new ChangelogProducer();

		Changelog log = producer.changesBetween(origin, changed);

		if (log.isEmpty()) {

			MODIFIED.removeFrom(attributes);

			return;

		}
		
		if (MODIFIED.isIn(attributes)) {

			List<ChangelogEntry> oldentries = jsonBinder().fromJson(MODIFIED.in(attributes), logtype);

			log.addAll(oldentries);

		} 
		
		List<ChangelogEntry> entries = log.entries();

		sort(entries);

		MODIFIED.set(jsonBinder().toJson(entries, logtype)).on(changed);

	}
}
