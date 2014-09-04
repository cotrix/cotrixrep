/**
 * 
 */
package org.cotrix.web.common.client.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UIFacet;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIConstraint;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class UIFactories {
	
	private static final String defaultCodeName = "name";
	private static final Language DEFAULT_LANGUAGE = Language.NONE;
	private static final Set<UIFacet> VISIBLE_SET = new HashSet<UIFacet>();
	public static final Set<UIFacet> EMPTY_SET = new HashSet<UIFacet>();
	
	static {
		VISIBLE_SET.add(UIFacet.VISIBLE);
	}
	
	@Inject
	private UIDefaults defaults;
	
	public UIAttributeDefinition createAttributeDefinition() {
		UIAttributeDefinition definition = new UIAttributeDefinition();
		definition.setType(defaults.defaultType());
		definition.setLanguage(DEFAULT_LANGUAGE);
		definition.setConstraints(new ArrayList<UIConstraint>());
		definition.setRange(defaults.defaultRange());
		definition.setFacets(Collections.<UIFacet>emptySet());
		return definition;
	}
	
	public UIAttribute createAttribute() {
		//TODO id?
		UIAttribute attribute = new UIAttribute();
		attribute.setType(defaults.defaultType());
		attribute.setLanguage(DEFAULT_LANGUAGE);
		attribute.setFacets(VISIBLE_SET);
		return attribute;
	}
	
	public UICode createCode() {
		//TODO id?
		UICode code = new UICode();
		UIQName name = new UIQName(defaults.defaultNameSpace(), defaultCodeName);
		code.setName(name);
		code.setAttributes(new ArrayList<UIAttribute>());
		code.setLinks(new ArrayList<UILink>());
		return code;
	}
	
	public UILinkDefinition createLinkDefinition() {
		UILinkDefinition definition = new UILinkDefinition();
		UIQName name = new UIQName(defaults.defaultNameSpace(), "");
		definition.setName(name);
		definition.setAttributes(new ArrayList<UIAttribute>());
		definition.setRange(defaults.defaultRange());
		return definition;
	}
	
	public UILink createLink() {
		UILink link = new UILink();
		link.setAttributes(new ArrayList<UIAttribute>());
		return link;
	}

}
