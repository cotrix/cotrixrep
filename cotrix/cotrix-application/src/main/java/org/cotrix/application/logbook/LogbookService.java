package org.cotrix.application.logbook;



public interface LogbookService {
	
	
	Logbook logbookOf(String id);

	interface Private extends LogbookService {
		
		void create(Logbook book);
		
		void removeLogbookOf(String id);

		void update(Logbook book);
		
	}

}
