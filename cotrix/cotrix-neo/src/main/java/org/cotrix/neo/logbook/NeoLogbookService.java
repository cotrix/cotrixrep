package org.cotrix.neo.logbook;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.common.Constants.*;
import static org.cotrix.neo.NeoNodeFactory.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.application.logbook.Logbook;
import org.cotrix.application.logbook.LogbookService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton @Alternative @Priority(RUNTIME)
public class NeoLogbookService implements LogbookService.Private {
	
	private static Logger log = LoggerFactory.getLogger(NeoLogbookService.class);
	
	@Inject
	private GraphDatabaseService store;
	
	
	
	@Override
	public void create(Logbook book) {
		
		notNull("logbook",book);
		
		Node node = store.createNode(LOGBOOK);
		
		node.setProperty(id_prop,book.resourceId());
		node.setProperty(entries_prop,binder().toXML(book.entries()));
	}
	
	@Override
	public Logbook logbookOf(String id) {
		
		notNull("logbook identifier",id);
		
		Node node = node(LOGBOOK,id);
		
		if (node==null)
			return null;
		
		@SuppressWarnings("all")
		List<Logbook.Entry> entries =  (List) binder().fromXML((String)node.getProperty(entries_prop));
		
		return new Logbook((String) node.getProperty(id_prop),entries);
	}
	
	@Override
	public void removeLogbookOf(String id) {
		
		notNull("logbook identifier",id);
	
		Node node = node(LOGBOOK,id);

		if (node==null) {
			log.warn("cannot remove codelist logbook for {} (maybe it has already been removed?)",id);
			return;
		}
		
		try {
			node.delete();
		}
		catch(Exception e) {
			rethrow("cannot remove codelist logbook for "+id,e);
		}
		
		log.info("removed {}'s logbook",id);
		
	}
	
	@Override
	public void update(Logbook book) {
		
		notNull("logbook",book);
		
		Node node = node(LOGBOOK,book.resourceId());

		if (node==null) 
			log.warn("cannot update codelist logbook for {} (maybe it has already been removed?)",book.resourceId());
		
		else {
		
			node.setProperty(entries_prop, binder().toXML(book.entries()));
		
			log.info("updated {}'s logbook",book.resourceId());
		}
		
	}

}
