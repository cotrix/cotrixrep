/**
 * 
 */
package org.cotrix.web.manage.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.utils.Constants;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.manage.shared.AttributeGroup;
import org.cotrix.web.manage.shared.Group;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupFactory {
	
	public static Set<Group> getAttributesGroups(Iterable<Code> rows)
	{
		Map<AttributeGroup, Integer> groupsCounter = new HashMap<AttributeGroup, Integer>();
		for (Code row:rows) {
			Map<AttributeGroup, Integer> attributeGroupsCounter = new HashMap<AttributeGroup, Integer>();
			for (Attribute attribute:row.attributes()) {
				AttributeGroup group = getAttributeGroup(attribute);
				
				Integer counter = attributeGroupsCounter.get(group);
				if (counter == null) counter = 0;
				counter++;
				attributeGroupsCounter.put(group, counter);
			}
			for (Entry<AttributeGroup, Integer> attributeGroupCounter:attributeGroupsCounter.entrySet()) {
				Integer groupCounter = groupsCounter.get(attributeGroupCounter.getKey());
				groupCounter = groupCounter == null?0:groupCounter;
				int count = Math.max(groupCounter, attributeGroupCounter.getValue());
				groupsCounter.put(attributeGroupCounter.getKey(), count);
			}
		}
		
		Set<Group> groups = new TreeSet<Group>();
		for (Entry<AttributeGroup, Integer> groupCounter:groupsCounter.entrySet()) {
			for (int i = 0; i < groupCounter.getValue(); i++) {
				AttributeGroup group = groupCounter.getKey().clone();
				group.setPosition(i);
				groups.add(group);
			}
		}

		return groups;
	}
	
	private static AttributeGroup getAttributeGroup(Attribute attribute)
	{
		boolean isSystemGroup = attribute.type()!=null?attribute.type().equals(Constants.SYSTEM_TYPE):false;
		return new AttributeGroup(ValueUtils.safeValue(attribute.name()), null, ValueUtils.safeValue(attribute.language()), isSystemGroup);
	}
}
