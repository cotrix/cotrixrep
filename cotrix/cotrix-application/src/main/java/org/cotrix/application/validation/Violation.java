package org.cotrix.application.validation;

import static org.cotrix.application.shared.EditorialEvent.Type.*;

import org.cotrix.application.shared.DefaultEditorialEvent;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.trait.Definition;

//extends editorial events to better capture intent, for future evolution, and as a home for factory methods.

public class Violation extends DefaultEditorialEvent {

	
	public static final Violation violation(Definition def) {
		return new Violation(def.qname().getLocalPart());
	}
	
	public static final Violation violation(Attribute a) {
		Violation v = new Violation(a.qname().getLocalPart());
		v.subtitle(a.language()==null? a.type().getLocalPart():a.type().getLocalPart()+","+a.language());
		return v;
	}
	
	
	public Violation(Object title) {
		super(ERROR,title);
	}
}
