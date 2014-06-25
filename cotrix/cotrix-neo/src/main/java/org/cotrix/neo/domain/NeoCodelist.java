package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.NodeType.*;

import java.util.HashMap;
import java.util.Map;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoContainer;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;

public class NeoCodelist extends NeoVersioned implements Codelist.State {

	public static final NeoStateFactory<Codelist.State> factory = new NeoStateFactory<Codelist.State>() {
		
		@Override
		public Codelist.State beanFrom(Node node) {
			return new NeoCodelist(node);
		}
		
		@Override
		public Node nodeFrom(Codelist.State state) {
			
			return new NeoCodelist(state).node();
		}
	};
	
	public NeoCodelist(Node node) {
		super(node);
	}
	
	public NeoCodelist(Codelist.State state) {

		super(CODELIST,state);
		
		Map<String, Definition.State> definitions = new HashMap<>();
		
		for (Definition.State l : state.attributeTypes()) {
			Node definitionNode = NeoDefinition.factory.nodeFrom(l);
			node().createRelationshipTo(definitionNode,Relations.DEFINITION);
			definitions.put(l.id(), NeoDefinition.factory.beanFrom(definitionNode));
		}
		
		for (CodelistLink.State l : state.links())
			node().createRelationshipTo(NeoCodelistLink.factory.nodeFrom(l),Relations.LINK);
		
		//update attribute definitions
		for (Code.State c : state.codes()) {
			for (Attribute.State a: c.attributes()) {
				Definition.State persistedState = definitions.get(a.definition().id());
				if (persistedState!=null) a.definition(persistedState);
			}
		}
		
		for (Code.State c : state.codes())
			node().createRelationshipTo(NeoCode.factory.nodeFrom(c),Relations.CODE);
		
		
	}
	
	public Codelist.Private entity() {
		return new Codelist.Private(this);
	}
	
	@Override
	public NamedStateContainer<Code.State> codes() {
		return new NeoContainer<>(node(),Relations.CODE,NeoCode.factory);
	}
	
	@Override
	public NamedStateContainer<CodelistLink.State> links() {
		return new NeoContainer<>(node(), Relations.LINK, NeoCodelistLink.factory);
	}
	
	@Override
	public NamedStateContainer<Definition.State> attributeTypes() {
		return new NeoContainer<>(node(), Relations.DEFINITION, NeoDefinition.factory);
	}
	
}
