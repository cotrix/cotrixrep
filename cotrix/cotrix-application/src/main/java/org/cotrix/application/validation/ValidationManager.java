package org.cotrix.application.validation;

import static java.lang.Math.*;
import static java.lang.System.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.common.async.TaskUpdate.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.cotrix.repository.CodelistQueries.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.CommonUtils;
import org.cotrix.common.async.CancelledTaskException;
import org.cotrix.common.async.TaskContext;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.domain.trait.Status;
import org.cotrix.repository.CodelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;


@Singleton
public class ValidationManager {

	private static Logger log = LoggerFactory.getLogger(ValidationManager.class);
	
	@Inject
	private CodelistRepository codelists;
	
	@Inject
	private Validator validator;
	
	public void checkPunctual(Codelist changeset) {
		
		//we know this will succeed, an update has already taken place 
		Codelist list = codelists.lookup(changeset.id());

		List<String> ids = new ArrayList<String>();
		
		for (Code.Private change : reveal(changeset).codes())
			if (change.status()!=Status.DELETED)
				ids.add(change.id());

		//we could iterate directly over the list but we query for changed code
		//to minimise object generation
		for (Code code :codelists.get(codes(ids)))
			 check(list,reveal(code));
		
	}
	
	public void checkBulk(Codelist list) {
		
		long time = currentTimeMillis();
		
		TaskContext context = new TaskContext();
		
		float progress=0f;
		
		NamedContainer<? extends Code.Private> codes =  reveal(list).codes();
		
		int total = list.codes().size();
		
		//arbitrary
		long step = round(max(10,floor(codes.size()/10)));
		
		int i=0;
		
		for (Code.Private code : codes) {
			
			check(list,code);
			
			i++;
			progress++;
			
			if (i%step==0)
				
				if (context.isCancelled()) {
					log.info("codelist update aborted on user request after {} codes.",i);
					throw new CancelledTaskException("codelist creation aborted on user request");
				}
			
				else {
				
					System.out.println("saving "+progress/total);
					context.save(update(progress/total, "validated "+i+" codes"));
				}
			
		}
		
		
		log.trace("validated codelist {} in {} msec.",list.id(),currentTimeMillis()-time);
		
	}
	
	private void check(Codelist list, Code.Private code) {
		
		NamedStateContainer<Attribute.State> attributes = code.state().attributes();
		
		List<Violation> violations = validator.check(list, code);
		
		ManagedCode managed = manage(code);
		
		Attribute invalid = managed.attribute(INVALID);
		
		if (violations.isEmpty()) {
			
			if (has(invalid))
				attributes.remove(invalid.id());
			
			return;
		}
		
		Type type = new TypeToken<List<Violation>>(){}.getType();
		
		if (hasno(invalid)) {
			
			attributes.add(stateof(attribute().instanceOf(INVALID)));
			invalid = managed.attribute(INVALID);
		}
		else {
			
			List<Violation> existingViolations = jsonBinder().fromJson(invalid.value(),type) ;
			
			//preserve user and timestamp of already-known errors
			for (Violation violation : existingViolations)
				if (violations.remove(violation))
					violations.add(violation);
			
			Collections.sort(violations);
			
		}
		
		stateof(invalid).value(CommonUtils.jsonBinder().toJson(violations,type));
	}
	
	
	
	// helpers
	
	private boolean has(Object o) {
		return o!=null;
	}
	
	private boolean hasno(Object o) {
		return o==null;
	}
}
