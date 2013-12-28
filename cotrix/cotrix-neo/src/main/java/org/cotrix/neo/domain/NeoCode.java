package org.cotrix.neo.domain;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Code.Private;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.common.StateContainer;
import org.cotrix.neo.domain.utils.NeoFactory;
import org.neo4j.graphdb.Node;

public class NeoCode extends NeoNamed implements Code.State {

	public static final NeoFactory<Code.State> factory = new NeoFactory<Code.State>() {
		
		@Override
		public Code.State beanFrom(Node node) {
			return new NeoCode(node);
		}
		
		@Override
		public Node nodeFrom(Code.State state) {
			return new NeoCode(state).node();
		}
	};
	
	public NeoCode(Node node) {
		super(node);
	}
	
	public NeoCode(Code.State state) {
		super(CODE,state);
	}
	
	@Override
	public Private entity() {
		return new Code.Private(this);
	}

	@Override
	public StateContainer<Codelink.State> links() {
		return beans();
	}

}
