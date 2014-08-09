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
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.trait.Status;
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

	
	public void trackAfter(Codelist changeset) {
	
		//we know this will succeed, an update has already taken place 
		Codelist list = codelists.lookup(changeset.id());
		
		//changelog requires lineage
		if (!list.attributes().contains(PREVIOUS_VERSION)) 		
			return;
		
		Codelist.Private plist = reveal(list);
		Codelist.Private pchangeset = reveal(changeset);
		
		trackPunctual(plist, pchangeset);
	}
	
	
	@Override
	public void track(Codelist list, boolean optimised) {
		
		TaskContext context = new TaskContext();

		float progress=0f;
		
		Container<? extends Code.Private> codes =  reveal(list).codes();
		
		int total = list.codes().size();
		
		//arbitrary
		long step = round(max(10,floor(codes.size()/10)));
		
		int i=0;
		
		long time = currentTimeMillis();
		
		Date listCreated = list.created();
		
		for (Code.Private code : codes) {
		
			i++;
			progress++;
			
			Date codeUpdated = code.lastUpdated();
			
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

	private void trackPunctual(Codelist.Private list, Codelist.Private changeset) {

		for (Code.Private change : changeset.codes()) {

			// changelog is pointless if the code is to be removed anyway
			if (change.status() == Status.DELETED)
				continue;

			Code.Private code = list.codes().lookup(change.id());

			trackCode(code,change);
		}
		
	}

	private void trackCode(Code.Private changed, Code.Private change) {

		if (change.status() == null)

			aaddNewMarkerTo(changed.bean().attributes());

		else

			handleModifiedMarkerWith(changed);

	}

	private void aaddNewMarkerTo(BeanContainer<Attribute.Bean> attributes) {

		attributes.add(stateof(attribute().instanceOf(NEW).value("TRUE")));
	}

	private void handleModifiedMarkerWith(Code.Private changed) {

		BeanContainer<Attribute.Bean> attributes = changed.bean().attributes();

		String originId = changed.originId();

		// by now we know codelist has lineage, but this update may be for a new
		// code
		if (notmarked(originId))
			return;
		
		
		Attribute modified = changed.attributes().getFirst(MODIFIED);

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
			modified = changed.attributes().getFirst(MODIFIED); // remember persistence

		}

		List<ChangelogEntry> entries = log.entries();

		sort(entries);

		stateof(modified).value(jsonBinder().toJson(entries, logtype));

	}

	private boolean marked(Object o) {
		return o != null;
	}

	private boolean notmarked(Object o) {
		return o == null;
	}
}
