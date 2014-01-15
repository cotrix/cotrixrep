package org.acme.domain;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.neo.NeoNodeFactory.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

import org.acme.SubjectProvider;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Attribute.State;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.NeoAttribute;
import org.cotrix.neo.domain.utils.NeoContainer;

@Alternative
@Priority(TEST)
public class NeoSubjectProvider implements SubjectProvider {

	
	@Override
	public NamedStateContainer<Attribute.State> like(Attribute.State... states) {
		NeoContainer<State> container = new NeoContainer<>(newnode(CODE), Relations.ATTRIBUTE, NeoAttribute.factory) ;
		for (Attribute.State state : states)
			container.add(state);
		return container;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T like(T object) {

		Object provided = null;

		if (object instanceof Codelist)
			provided = new Codelist.Private(reveal(object, Codelist.Private.class).state());

		else if (object instanceof Code)
			provided = new Code.Private(reveal(object, Code.Private.class).state());

		else if (object instanceof Attribute)
			provided = new Attribute.Private(reveal(object, Attribute.Private.class).state());

		else if (object instanceof Named) {
			Named.State s = (Named.State) reveal(object, Named.Abstract.class).state();
			provided = new Named.Abstract(s) {
			};
		}

		else if (object instanceof Attributed) {
			Attributed.State s = (Attributed.State) reveal(object, Attributed.Abstract.class).state();
			provided = new Attributed.Abstract(s) {
			};
		}

		else if (object instanceof Identified)
			provided = new Identified.Abstract(reveal(object, Identified.Abstract.class).state()) {
			};

		else
			throw new IllegalArgumentException("cannot provide test subject for " + object);

		return (T) provided;
	}

}
