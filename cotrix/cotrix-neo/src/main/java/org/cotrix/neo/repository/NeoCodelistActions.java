package org.cotrix.neo.repository;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;
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
	public UpdateAction<Codelist> deleteDefinition(final String definitionId) {
		
		return new UpdateAction<Codelist>() {
			
			@Override
			public void performOver(Codelist list) {
				
				if (!list.definitions().contains(definitionId))
					throw new IllegalArgumentException("no definition "+definitionId+" in list "+list.id()+" ("+list.name()+")");
				
					
				Node node = node(NodeType.DEFINITION, definitionId);
				
				for (Relationship instanceRelationship : node.getRelationships(INCOMING,INSTANCEOF))
					try {
						removeNode(instanceRelationship.getStartNode());
					}
					catch(Exception e) {
						rethrow("cannot delete definition instance (see cause)", e);
					}
				
				try {
					removeNode(node);
				}
				catch(Exception e) {
					rethrow("cannot delete definition instance (see cause)", e);
				}
				
			}
			
			public String toString() {
				return "action [delete definition "+definitionId;
			}
		};
	}

	
}
