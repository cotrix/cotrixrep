package org.cotrix.application.changelog;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
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
import org.cotrix.domain.managed.ManagedCodelist;
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
		
		ManagedCodelist mlist = ManagedCodelist.manage(list);
		
		//changelog is only wrt to lineage
		if (mlist.originId()==null)
			return;
		
		
		log.trace("updating changelog for codelist {}",changeset.id());
		
		for (Code codeChangeset : changeset.codes()) {
			
			Code.Private privateCodeChangeset = reveal(codeChangeset);
			
			//only if it bring real changes
			if (privateCodeChangeset.status()==Status.DELETED || privateCodeChangeset.attributes().contains(DELETED))
				continue;
			
			Code code = list.codes().lookup(codeChangeset.id());
			
			Code.Private privateCode = reveal(code);
			
			NamedStateContainer<Attribute.State>  attributes = privateCode.state().attributes();
			
			//new 
			if (privateCodeChangeset.status()==null)
				attributes.add(stateof(attribute().with(NEW).value("true")));
			
			//modified
			else {
				
				ManagedCode managed = manage(code);
				
				Attribute modified = managed.attribute(MODIFIED);
				
				if (modified==null) {
					modified = attribute().with(MODIFIED).value("TRUE").build();
					attributes.add(stateof(modified));
				}
				
				String originId = managed.originId();
				
				Code origin = codelists.get(code(originId));
				
				if (origin==null) {
					log.error("application error: cannot compute changelog for code {} as its lineage {} can't be retrieved.",codeChangeset.id(),originId);
					continue;
				}
				
				List<CodeChange> changes = detect.changesBetween(origin, codeChangeset);
				
				reveal(modified).state().description(changes.toString());
				
			}
			
		}
			
	}
}
