package org.cotrix.neo.repository;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.neo.NeoNodeFactory.*;
import static org.cotrix.neo.NeoUtils.*;
import static org.cotrix.neo.domain.Constants.Relations.*;
import static org.neo4j.graphdb.Direction.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.neo.domain.Constants.NodeType;
import org.cotrix.repository.UpdateAction;
import org.cotrix.repository.spi.CodelistActionFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

@Singleton
@Alternative
@Priority(RUNTIME)
public class NeoCodelistActions extends NeoQueries implements CodelistActionFactory {

	
	@Override
	public UpdateAction<Codelist> deleteCodelistLink(final String linkId) {
		
		return new UpdateAction<Codelist>() {
			
			@Override
			public void performOver(Codelist list) {
				
				if (!list.links().contains(linkId))
					throw new IllegalArgumentException("no link definition "+linkId+" in list "+list.id()+" ("+list.qname()+")");
				
					
				Node node = node(NodeType.LINKDEF, linkId);
				
				for (Relationship instanceRelationship : node.getRelationships(INCOMING,INSTANCEOF))
					try {
						removeNode(instanceRelationship.getStartNode());
					}
					catch(Exception e) {
						rethrow("cannot delete link definition instance (see cause)", e);
					}
				
				try {
					removeNode(node);
				}
				catch(Exception e) {
					rethrow("cannot delete link definition (see cause)", e);
				}
				
			}
			
			public String toString() {
				return "action [delete link definition "+linkId;
			}
		};
	}
	
	
	@Override
	public UpdateAction<Codelist> deleteDefinition(final String definitionId) {
		
		return new UpdateAction<Codelist>() {
			
			@Override
			public void performOver(Codelist list) {
				
				if (!list.definitions().contains(definitionId))
					throw new IllegalArgumentException("no attribute definition "+definitionId+" in list "+list.id()+" ("+list.qname()+")");
				
					
				Node node = node(NodeType.ATTRDEF, definitionId);
				
				for (Relationship instanceRelationship : node.getRelationships(INCOMING,INSTANCEOF))
					try {
						removeNode(instanceRelationship.getStartNode());
					}
					catch(Exception e) {
						rethrow("cannot attribute definition instance (see cause)", e);
					}
				
				try {
					removeNode(node);
				}
				catch(Exception e) {
					rethrow("cannot delete attribute definition (see cause)", e);
				}
				
			}
			
			public String toString() {
				return "action [delete attribute definition "+definitionId;
			}
		};
	}

	
}
