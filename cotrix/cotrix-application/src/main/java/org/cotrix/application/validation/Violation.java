package org.cotrix.application.validation;

import static org.cotrix.application.changelog.EditorialEvent.Type.*;

import org.cotrix.application.changelog.DefaultEditorialEvent;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.trait.Definition;


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
