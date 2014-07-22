/**
 * 
 */
package org.cotrix.web.common.client.factory;

import java.util.ArrayList;

import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
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
	
	@Inject
	private UIDefaults defaults;
	
	public UIAttributeDefinition createAttributeType() {
		UIAttributeDefinition type = new UIAttributeDefinition();
		type.setType(defaults.defaultType());
		type.setLanguage(DEFAULT_LANGUAGE);
		type.setConstraints(new ArrayList<UIConstraint>());
		type.setRange(defaults.defaultRange());
		return type;
	}
	
	public UIAttribute createAttribute() {
		//TODO id?
		UIAttribute attribute = new UIAttribute();
		attribute.setType(defaults.defaultType());
		attribute.setLanguage(DEFAULT_LANGUAGE);
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
	
	public UILinkDefinition createLinkType() {
		UILinkDefinition linkType = new UILinkDefinition();
		UIQName name = new UIQName(defaults.defaultNameSpace(), "");
		linkType.setName(name);
		linkType.setAttributes(new ArrayList<UIAttribute>());
		return linkType;
	}
	
	public UILink createLink() {
		UILink link = new UILink();
		link.setAttributes(new ArrayList<UIAttribute>());
		return link;
	}

}
