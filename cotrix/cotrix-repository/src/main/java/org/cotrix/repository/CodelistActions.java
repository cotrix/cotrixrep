package org.cotrix.repository;

import static org.cotrix.common.Utils.*;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents;
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
	

	
	static class ActionFactoryInjector {

		void configure(@Observes ApplicationEvents.Startup event, CodelistActionFactory factory) {	
			
			CodelistActions.setFactory(factory);
		}
	}
}
