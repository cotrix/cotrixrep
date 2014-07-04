package org.cotrix.repository;

import static org.cotrix.common.CommonUtils.*;

import javax.enterprise.event.Observes;

import org.cotrix.common.events.ApplicationLifecycleEvents;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.spi.CodelistActionFactory;

public class CodelistActions {

	private static CodelistActionFactory factory;
	
	public static void setFactory(CodelistActionFactory factory) {
		CodelistActions.factory = factory;
	}
	
	
	
	public static UpdateAction<Codelist> deleteDefinition(String definitionId) {
		
		notNull("definition identifier", definitionId);
		
		return factory.deleteDefinition(definitionId);
	}
	
	public static UpdateAction<Codelist> deleteCodelistLink(String linkId) {
		
		notNull("codelist link identifier", linkId);
		
		return factory.deleteCodelistLink(linkId);
	}

	
	static class ActionFactoryInjector {

		void configure(@Observes ApplicationLifecycleEvents.Startup event, CodelistActionFactory factory) {	
			
			CodelistActions.setFactory(factory);
		}
	}
}
