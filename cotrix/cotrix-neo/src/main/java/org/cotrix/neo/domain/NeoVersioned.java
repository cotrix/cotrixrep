package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.*;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;
import org.cotrix.neo.domain.Constants.NodeType;
import org.neo4j.graphdb.Node;

public class NeoVersioned extends NeoNamed implements Versioned.State {


	public NeoVersioned(Node node) {
		super(node);
	}
	
	public <S extends Named.State & Attributed.State & Versioned.State> NeoVersioned(NodeType type, S state) {
		super(type,state);
		
		version(state.version());
		
	}
	
	@Override
	public Version version() {
		return new DefaultVersion((String) node().getProperty(version_prop));
	}

	@Override
	public void version(Version version) {
		node().setProperty(version_prop, version.value());
	}
	
}
