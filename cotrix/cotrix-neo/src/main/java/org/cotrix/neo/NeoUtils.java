package org.cotrix.neo;

import static org.cotrix.common.Utils.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.Relations.*;
import static org.neo4j.graphdb.Direction.*;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.kernel.impl.cache.LruCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class NeoUtils {
	
	//gotta to exceed a reasonable expectation of large 'attribute set'.
	private static final int thread_cache_size = 50;

	private static Logger log = LoggerFactory.getLogger(NeoUtils.class);
	
	private static XStream stream = new XStream();
	
	
	//the intention here is to serve a single import task, which we know takes place on a single thread
	private static InheritableThreadLocal<LruCache<String,Node>> cache;
	
	
	
	public static void removeEntityNode(Node n) {
		
		notNull("node",n);
		
		if (n.hasRelationship(INCOMING))
			throw new IllegalStateException("entity "+n.getProperty(id_prop)+":"+n.getLabels()+" cannot be removed: there are other entities that link to it, directly or indirectly");
		
		removeNode(n);
					
	}
	
	public static void removeNode(Node n) {
		
		notNull("node",n);
		
		for (Relationship r : n.getRelationships(OUTGOING))
			
			//we do not remove links and shared types
			if (!r.isType(LINK) && !r.isType(INSTANCEOF))
				removeNode(r.getEndNode());
			else
				r.delete();
		
		for (Relationship r : n.getRelationships(INCOMING))
			r.delete();
		
		n.delete();
		
			
	}
	
	public static XStream binder() {
		return stream; 
	}
	
	
	
	public static LruCache<String,Node> threadCache() {
		
		return cache.get();
	}
	
	static void prepareCaches(@Observes Startup event){
		
		cache = new InheritableThreadLocal<LruCache<String,Node>>() {
			
			protected LruCache<String,Node> initialValue() {return new LruCache<String, Node>("thread-cache",thread_cache_size);}
		
		};
		
		log.info("prepared thread caches");
		

	}

	static void detachCaches(@Observes Shutdown event){
		
		cache=null;
		
		log.info("detached thread caches");
		

	}
}
