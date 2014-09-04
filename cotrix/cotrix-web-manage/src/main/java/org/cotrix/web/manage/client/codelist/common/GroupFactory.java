/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			//FIXME
			int max = range.getMax() == Integer.MAX_VALUE?1:range.getMax();
			for (int i = 0; i<max; i++) {
				AttributeGroup group = new AttributeGroup(definition);
				group.setPosition(i);
				groups.add(group);
			}
		}
		
		for (UILinkDefinition definition:linkTypesCache) {
			int max = 1;
			for (int i = 0; i<max; i++) {
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

}
