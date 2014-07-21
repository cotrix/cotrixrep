package org.acme.domain;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.neo.NeoNodeFactory.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

import org.acme.SubjectProvider;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.attributes.Attribute.State;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.user.User;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.NeoAttribute;
import org.cotrix.neo.domain.NeoDefinition;
import org.cotrix.neo.domain.NeoCode;
import org.cotrix.neo.domain.NeoCodelink;
import org.cotrix.neo.domain.NeoCodelist;
import org.cotrix.neo.domain.NeoCodelistLink;
import org.cotrix.neo.domain.NeoUser;
import org.cotrix.neo.domain.utils.NeoContainer;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;

@Alternative
@Priority(TEST)
public class NeoSubjectProvider implements SubjectProvider {

	@Override
	public NamedStateContainer<Attribute.State> like(Attribute.State... states) {
		NeoContainer<State> container = new NeoContainer<>(newnode(CODE), Relations.ATTRIBUTE, NeoAttribute.factory);
		for (Attribute.State state : states)
			container.add(state);
		return container;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T like(T object) {

		Object provided = null;
		
		if (object instanceof User)
			provided = new User.Private(like(object, User.Private.class,NeoUser.factory));

		else if (object instanceof Codelist) {
			provided = new Codelist.Private(like(object,Codelist.Private.class,NeoCodelist.factory));

		}
		else if (object instanceof Code)
			provided = new Code.Private(like(object, Code.Private.class,NeoCode.factory));

		else if (object instanceof Attribute)
			provided = new Attribute.Private(like(object, Attribute.Private.class,NeoAttribute.factory));
		
		else if (object instanceof AttributeDefinition)
			provided = new AttributeDefinition.Private(like(object, AttributeDefinition.Private.class,NeoDefinition.factory));

		else if (object instanceof LinkDefinition)
			provided = new LinkDefinition.Private(like(object,LinkDefinition.Private.class,NeoCodelistLink.factory)); 

		else if (object instanceof Codelink)
			provided = new Codelink.Private(like(object, Codelink.Private.class,NeoCodelink.factory));

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
	
	//helper
	
	private <S extends Identified.State,T extends Identified.Abstract<T,S>> S like(Object o, Class<T> type, NeoStateFactory<S> factory) {
		
		S state = reveal(o,type).state();
		Node node = factory.nodeFrom(state);
		return factory.beanFrom(node);
	}

}
