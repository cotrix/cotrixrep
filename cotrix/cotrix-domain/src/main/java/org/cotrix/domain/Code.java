package org.cotrix.domain;

import org.cotrix.domain.primitive.container.Container;
import org.cotrix.domain.primitive.entity.NamedEntity;
import org.cotrix.domain.primitive.link.CodeLink;



/**
 * A code.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Code extends NamedEntity<Code> {
	
	Container<CodeLink> links();
}