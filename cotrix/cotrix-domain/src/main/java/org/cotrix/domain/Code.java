package org.cotrix.domain;

import org.cotrix.domain.primitive.container.Container;
import org.cotrix.domain.primitive.entity.AttributedEntity;
import org.cotrix.domain.primitive.link.CodeLink;



/**
 * A named and attributed code.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Code extends AttributedEntity<Code> {
	
	Container<CodeLink> links();
}