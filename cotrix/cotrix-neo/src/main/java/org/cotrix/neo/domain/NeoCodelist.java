package org.cotrix.neo.domain;

import static java.lang.Math.*;
import static org.cotrix.common.async.TaskUpdate.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import org.cotrix.common.async.CancelledTaskException;
import org.cotrix.common.async.TaskContext;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoContainer;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeoCodelist extends NeoVersioned implements Codelist.Bean {
	
	private static final Logger log = LoggerFactory.getLogger(NeoCodelist.class);

	public static final NeoStateFactory<Codelist.Bean> factory = new NeoStateFactory<Codelist.Bean>() {
		
		@Override
		public Codelist.Bean beanFrom(Node node) {
			return new NeoCodelist(node);
		}
		
		@Override
		public Node nodeFrom(Codelist.Bean state) {
			
			return new NeoCodelist(state).node();
		}
	};
	
	public NeoCodelist(Node node) {
		super(node);
	}
	
	
	public NeoCodelist(Codelist.Bean state) {
		
		super(CODELIST,state);	
	
		TaskContext context = new TaskContext();
		
		float progress=0f;
		
		int total = state.codes().size()+state.definitions().size()+state.links().size();
		
		for (AttributeDefinition.State def : state.definitions())
			node().createRelationshipTo(NeoAttributeDefinition.factory.nodeFrom(def),Relations.DEFINITION);
		
		progress = progress + state.definitions().size();
		
		context.save(update(progress/total, "added "+state.definitions().size()+" definitions"));
		
		for (LinkDefinition.State l : state.links())
			node().createRelationshipTo(NeoLinkDefinition.factory.nodeFrom(l),Relations.LINK);
		
		progress = progress + state.links().size();
		
		context.save(update(progress/total, "added "+state.links().size()+" links"));
		
		int i = 0;
	
		int codes = state.codes().size();
		
		//arbitrary
		long step = round(max(10,floor(codes/10)));
		
		for (Code.Bean c : state.codes()) {
			
			if (i%step==0)
			
				if (context.isCancelled()) {
					log.info("codelist creation aborted on user request after {} codes.",i);
					throw new CancelledTaskException("codelist creation aborted on user request");
				}
			
				else
				
					context.save(update(progress/total, "added "+i+" codes"));
		
				
			
			node().createRelationshipTo(NeoCode.factory.nodeFrom(c),Relations.CODE);
			
			i++;
			progress++;
		}
	}
	
	public Codelist.Private entity() {
		return new Codelist.Private(this);
	}
	
	@Override
	public BeanContainer<Code.Bean> codes() {
		return new NeoContainer<>(node(),Relations.CODE,NeoCode.factory);
	}
	
	@Override
	public BeanContainer<LinkDefinition.State> links() {
		return new NeoContainer<>(node(), Relations.LINK, NeoLinkDefinition.factory);
	}
	
	@Override
	public BeanContainer<AttributeDefinition.State> definitions() {
		return new NeoContainer<>(node(), Relations.DEFINITION, NeoAttributeDefinition.factory);
	}
	
}
