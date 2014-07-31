package org.cotrix.application.validation;

import static java.lang.System.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.CommonUtils;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.managed.ManagedCode;
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
	
	public void check(Codelist changeset) {
		
		Codelist.Private pchangeset = reveal(changeset);
		
		if (isBulkUpdate(pchangeset))
			check(pchangeset.id());
		
		else {
		
			//we know this will succeed, an update has already taken place 
			Codelist list = codelists.lookup(changeset.id());
			
			List<String> ids = new ArrayList<String>();
			
			for (Code change : changeset.codes())
				ids.add(change.id());
	
			for (Code.Private code : reveal(list).codes())
				if (ids.contains(code.id()))
					check(list,code);
		
		}
	}
	
	public void check(String id) {
		
		long time = currentTimeMillis();
		
		//we know this will succeed, an update has already taken place 
		Codelist list = codelists.lookup(id);
		
		for (Code.Private code : reveal(list).codes())
			check(list,code);
		
		log.trace("validated codelist {} in {} msec.",id,currentTimeMillis()-time);
		
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
}
