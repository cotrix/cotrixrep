/**
 * 
 */
package org.cotrix.web.share.server.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.DefaultLifecycleStates;
import org.cotrix.web.share.shared.codelist.LifecycleState;
import org.cotrix.web.share.shared.codelist.UICode;
import org.cotrix.web.share.shared.codelist.UICodelist;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;
import org.cotrix.web.share.shared.codelist.UIAttribute;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Codelists {

	public static final Comparator<Codelist> NAME_COMPARATOR = new Comparator<Codelist>() {

		@Override
		public int compare(Codelist o1, Codelist o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.name().getLocalPart(), o2.name().getLocalPart());
		}
	};

	public static final Comparator<Codelist> VERSION_COMPARATOR = new Comparator<Codelist>() {

		@Override
		public int compare(Codelist o1, Codelist o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.version(), o2.version());
		}
	};
	
	public static UICodelistMetadata toCodelistMetadata(Codelist codelist, LifecycleState state)
	{
		UICodelistMetadata metadata = toCodelistMetadata(codelist);
		metadata.setState(state);
		return metadata;
	}

	public static UICodelistMetadata toCodelistMetadata(Codelist codelist)
	{
		UICodelistMetadata metadata = new UICodelistMetadata();
		metadata.setId(codelist.id());
		metadata.setName(ValueUtils.safeValue(codelist.name()));
		metadata.setVersion(codelist.version());
		metadata.setAttributes(toUIAttributes(codelist.attributes()));
		return metadata;
	}

	public static UICodelist toUICodelist(Codelist codelist, State state) {
		UICodelist uiCodelist = toUICodelist(codelist);
		uiCodelist.setState(getLifecycleState(state));
		return uiCodelist;
	}
	
	protected static LifecycleState getLifecycleState(State state) {
		if (!(state instanceof DefaultLifecycleStates)) throw new IllegalArgumentException("Inconvertible state "+state);
		DefaultLifecycleStates defaultLifecycleStates = (DefaultLifecycleStates)state;
		switch (defaultLifecycleStates) {
			case draft: return LifecycleState.draft;
			case locked: return LifecycleState.locked;
			case sealed: return LifecycleState.sealed;
		}
		throw new IllegalArgumentException("Unknown default lifecycle state "+defaultLifecycleStates);
	}

	public static UICodelist toUICodelist(Codelist codelist) {
		UICodelist uiCodelist = new UICodelist();
		uiCodelist.setId(codelist.id());
		uiCodelist.setName(codelist.name().getLocalPart());
		uiCodelist.setVersion(codelist.version());

		return uiCodelist;
	}

	public static List<UIAttribute> toUIAttributes(List<? extends Attribute> attributes)
	{
		return toUIAttributes(attributes, attributes.size());
	}

	public static List<UIAttribute> toUIAttributes(Container<? extends Attribute> attributesContainer)
	{
		return toUIAttributes(attributesContainer, attributesContainer.size());
	}

	public static List<UIAttribute> toUIAttributes(Iterable<? extends Attribute> attributesContainer, int size)
	{
		List<UIAttribute> attributes = new ArrayList<UIAttribute>(size);

		for (Attribute domainAttribute:attributesContainer) {
			UIAttribute attribute = toUIAttribute(domainAttribute);
			attributes.add(attribute);
		}

		return attributes;
	}

	public static UIAttribute toUIAttribute(Attribute attribute)
	{
		UIAttribute uiattribute = new UIAttribute();
		uiattribute.setName(ValueUtils.safeValue(attribute.name()));
		uiattribute.setType(ValueUtils.safeValue(attribute.type()));
		uiattribute.setLanguage(ValueUtils.safeValue(attribute.language()));
		uiattribute.setValue(ValueUtils.safeValue(attribute.value()));
		uiattribute.setId(ValueUtils.safeValue(attribute.id()));
		return uiattribute;
	}
	
	public static UICode toUiCode(Code code) {
		if (code == null) return null;
		
		UICode uicode = new UICode();
		uicode.setId(code.id());
		uicode.setName(ValueUtils.safeValue(code.name()));

		List<UIAttribute> attributes = Codelists.toUIAttributes(code.attributes());
		uicode.setAttributes(attributes);
		return uicode;
	}


}
