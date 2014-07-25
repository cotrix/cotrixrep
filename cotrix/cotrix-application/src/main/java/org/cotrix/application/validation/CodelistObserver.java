package org.cotrix.application.validation;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.repository.CodelistQueries.*;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.events.Modified;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;

@Singleton
public class CodelistObserver {
	
	@Inject
	private CodelistRepository codelists;

	@Inject
	Validator validator;

	public void onCodelistUpdate(@Observes @Modified Codelist changeset){
		
		List<String> ids = new ArrayList<>();
		
		for (Code code : changeset.codes())
			if (reveal(code).status()!=DELETED)
				ids.add(code.id());
		
		Iterable<Code> codes = codelists.get(codes(ids));
		
		validator.check(codes);
		
	}
}
