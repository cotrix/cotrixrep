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
import org.cotrix.domain.attributes.Attribute.Bean;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.user.User;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.NeoAttribute;
import org.cotrix.neo.domain.NeoAttributeDefinition;
import org.cotrix.neo.domain.NeoCode;
import org.cotrix.neo.domain.NeoLink;
import org.cotrix.neo.domain.NeoCodelist;
import org.cotrix.neo.domain.NeoLinkDefinition;
import org.cotrix.neo.domain.NeoUser;
import org.cotrix.neo.domain.utils.NeoContainer;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;

@Alternative
@Priority(TEST)
public class NeoSubjectProvider implements SubjectProvider {

	@Override
	public BeanContainer<Attribute.Bean> like(Attribute.Bean... states) {
		NeoContainer<Bean> container = new NeoContainer<>(newnode(CODE), Relations.ATTRIBUTE, NeoAttribute.factory);
		for (Attribute.Bean state : states)
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
			provided = new AttributeDefinition.Private(like(object, AttributeDefinition.Private.class,NeoAttributeDefinition.factory));

		else if (object instanceof LinkDefinition)
			provided = new LinkDefinition.Private(like(object,LinkDefinition.Private.class,NeoLinkDefinition.factory)); 

		else if (object instanceof Link)
			provided = new Link.Private(like(object, Link.Private.class,NeoLink.factory));

		else if (object instanceof Attributed) {
			Attributed.Bean s = (Attributed.Bean) reveal(object, Attributed.Private.class).bean();
			provided = new Attributed.Private(s) {
			};
		}
		
		else if (object instanceof Named) {
			Named.Bean s = (Named.Bean) reveal(object, Named.Private.class).bean();
			provided = new Named.Private(s) {
			};
		}
		
		else if (object instanceof Identified)
			provided = new Identified.Private(reveal(object, Identified.Private.class).bean()) {
			};

		else
			throw new IllegalArgumentException("cannot provide test subject for " + object);

		return (T) provided;
	}
	
	//helper
	
	private <S extends Identified.Bean,T extends Identified.Private<T,S>> S like(Object o, Class<T> type, NeoStateFactory<S> factory) {
		
		S state = reveal(o,type).bean();
		Node node = factory.nodeFrom(state);
		return factory.beanFrom(node);
	}

}
