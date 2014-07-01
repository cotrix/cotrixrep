package org.cotrix.application.logbook;

import static org.cotrix.common.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;

import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.repository.CodelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 * 
 */
@ApplicationScoped @Alternative @Priority(DEFAULT)
public class MLogbookService implements LogbookService.Private {

	private static Logger log = LoggerFactory.getLogger(MLogbookService.class);

	private final Map<String,Logbook> books = new HashMap<String,Logbook>();
	
	@Override
	public void create(Logbook book) {
		
		books.put(book.resourceId(),book);
		
	}
	
	@Override
	public Logbook logbookOf(String id) {
		
		return books.get(id);
	}
	
	@Override
	public void update(Logbook logbook) {
		
		Logbook book = books.get(logbook.resourceId());
		
		if (book==null)
			throw new AssertionError("attempt to update transient logbook "+logbook.resourceId());
		
		log.info("updated logbook for {} ",logbook.resourceId());
	}
	
	@Override
	public void removeLogbookOf(String id) {
		
		if (books.remove(id)==null)
			throw new AssertionError("attempt to remove transient logbook "+id);
	}
	
	
	//helpers
	
	public void clear(@Observes Shutdown event) {
		books.clear();
	}
	
}
