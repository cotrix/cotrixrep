package org.cotrix.application.logbook;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.application.logbook.LogbookEvent.*;
import static org.cotrix.application.managed.ManagedCodelist.*;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.action.events.CodelistActionEvents.CodelistEvent;
import org.cotrix.action.events.CodelistActionEvents.Import;
import org.cotrix.action.events.CodelistActionEvents.Publish;
import org.cotrix.action.events.CodelistActionEvents.Version;
import org.cotrix.common.events.New;
import org.cotrix.common.events.Removed;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.lifecycle.LifecycleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodelistObserver {
	
	private static Logger log = LoggerFactory.getLogger(CodelistObserver.class);
	
	@Inject
	private LogbookService.Private service;

	//create
	public void onCodelistCreated(@Observes @New Codelist list) {
		
		try {
			
			Logbook book  = new Logbook(list.id());
			
			book.add(CREATED.entry());
			
			String previousVersion = manage(list).previousVersion();
			
			if (previousVersion!=null)
				book.add(VERSIONED.entryWith("versioned from:"+previousVersion));
			
			service.create(book);
			
			log.trace("created logbook for codelist {}",signatureOf(list));
			
			
		}
		catch(RuntimeException e) {
			log.error("cannot create logbook for codelist {}",signatureOf(list));
			throw e; //listeners are blocking, we can make failures blocking too: don't want a codelist without a logbook
		}
		
	}
	
	//remove
	public void onCodelistRemoved(@Observes @Removed Codelist list) {
		
		try {
			
			Logbook book = service.logbookOf(list.id());
			
			if (book == null)
				log.warn("cannot find logbook of codelist being removed {}",signatureOf(list));
			
			else {
			
				service.removeLogbookOf(list.id());
				
				log.trace("removed logbook for codelist",signatureOf(list));
			
			}
		}
		catch(RuntimeException e) {
			log.error("cannot remove logbook for codelist {}"+signatureOf(list),e);
		}
		
	}
	
	
	//imports
	public void onCodelistImported(@Observes Import event) {
		
		Logbook book = service.logbookOf(event.codelistId);
		
		if (book==null)
			log.warn("no logbook for imported codelist {}",signatureOf(event));
		
		else {
			
			book.add(IMPORTED.entryWith(event.origin));
			
			service.update(book);
			
		}
	}
	
	//state change
	public void onLifecycleChange(@Observes LifecycleEvent event) {
		
		Logbook book = service.logbookOf(event.resourceId());
		
		if (book==null)
			log.warn("no logbook for changed codelist {}",event.resourceId());
		
		else  {
			if (event.action().equals(SEAL.on(event.resourceId()))) {
				book.add(SEALED.entry());
				service.update(book);
				
			}
			else
				if (event.action().equals(LOCK.on(event.resourceId()))) {
					book.add(LOCKED.entry());
					service.update(book);
				}
		}
	}
	
	public void onCodelistPublished(@Observes Publish event) {
		
		Logbook book = service.logbookOf(event.codelistId);
		
		if (book==null)
			log.warn("no logbook for published codelist {}",signatureOf(event));
		
		else {
			
			book.add(PUBLISHED.entryWith(event.repository));
			
			service.update(book);
			
		}
	}
	
	public void onCodelistVersioned(@Observes Version event) {
		
		Logbook book = service.logbookOf(event.oldId);
		
		if (book==null)
			log.warn("no logbook for versioned codelist {}",signatureOf(event));
		
		else {
			
			book.add(VERSIONED.entryWith("versioned to:"+event.codelistVersion));
			
			service.update(book);
			
		}
	}
	
	//helpers
	private String signatureOf(Codelist list) {
		return signatureOf(list.id(),list.qname(),list.version());
	}
	
	private String signatureOf(CodelistEvent event) {
		return signatureOf(event.codelistId,event.codelistName, event.codelistVersion);
	}
	
	private String signatureOf(String id,QName name, String version) {
		return String.format("%s  (%s v.%s)",id,name,version);
	}
}
