/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.common.shared.codelist.Definition;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;
import org.cotrix.web.manage.client.codelist.cache.LinkTypesCache;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.shared.AttributeGroup;
import org.cotrix.web.manage.shared.Group;
import org.cotrix.web.manage.shared.LinkGroup;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupFactory {

	@Inject @CurrentCodelist
	private LinkTypesCache linkTypesCache;

	@Inject @CurrentCodelist
	private AttributeDefinitionsCache attributeTypesCache;

	public List<Group> getGroups() {
		List<Group> groups = new ArrayList<Group>();

		for (UIAttributeDefinition definition:attributeTypesCache) {
			UIRange range = definition.getRange();
			for (int i = 0; i<range.getMax(); i++) {
				AttributeGroup group = new AttributeGroup(definition);
				group.setPosition(i);
				groups.add(group);
			}
		}

		for (UILinkDefinition definition:linkTypesCache) {
			UIRange range = definition.getRange();
			for (int i = 0; i<range.getMax(); i++) {
				LinkGroup group = new LinkGroup(definition);
				group.setPosition(i);
				groups.add(group);
			}
		}

		return groups;
	}

	public Map<String, Group> getUniqueGroups() {
		Map<String, Group> groups = new HashMap<String, Group>();

		for (UIAttributeDefinition definition:attributeTypesCache) {
			AttributeGroup group = new AttributeGroup(definition);
			groups.put(definition.getId(), group);
		}

		for (UILinkDefinition definition:linkTypesCache) {
			LinkGroup group = new LinkGroup(definition);
			groups.put(definition.getId(), group);
		}

		return groups;
	}

	public List<Group> expandGroups(List<Group> groups) {
		List<Group> expandedGroups = new ArrayList<Group>();

		for (Group group:groups) {
			Definition definition = group.getDefinition();

			for (int i = 0; i<definition.getRange().getMax(); i++) {
				Group expandedGroup = group.cloneGroup();
				expandedGroup.setPosition(i);
				expandedGroups.add(expandedGroup);
			}
		}

		return expandedGroups;
	}


}
