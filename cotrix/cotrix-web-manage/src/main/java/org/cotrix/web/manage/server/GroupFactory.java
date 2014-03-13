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
import org.cotrix.web.manage.shared.Group;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupFactory {
	
	public static Set<Group> getGroups(Iterable<Code> rows)
	{
		Map<Group, Integer> groupsCounter = new HashMap<Group, Integer>();
		for (Code row:rows) {
			Map<Group, Integer> attributeGroupsCounter = new HashMap<Group, Integer>();
			for (Attribute attribute:row.attributes()) {
				Group group = getGroup(attribute);
				
				Integer counter = attributeGroupsCounter.get(group);
				if (counter == null) counter = 0;
				counter++;
				attributeGroupsCounter.put(group, counter);
			}
			for (Entry<Group, Integer> attributeGroupCounter:attributeGroupsCounter.entrySet()) {
				Integer groupCounter = groupsCounter.get(attributeGroupCounter.getKey());
				groupCounter = groupCounter == null?0:groupCounter;
				int count = Math.max(groupCounter, attributeGroupCounter.getValue());
				groupsCounter.put(attributeGroupCounter.getKey(), count);
			}
		}
		
		Set<Group> groups = new TreeSet<Group>();
		for (Entry<Group, Integer> groupCounter:groupsCounter.entrySet()) {
			for (int i = 0; i < groupCounter.getValue(); i++) {
				Group group = groupCounter.getKey().clone();
				group.setPosition(i);
				groups.add(group);
			}
		}

		return groups;
	}
	
	public static Group getGroup(Attribute attribute)
	{
		boolean isSystemGroup = attribute.type()!=null?attribute.type().equals(Constants.SYSTEM_TYPE):false;
		return new Group(ValueUtils.safeValue(attribute.name()), null, ValueUtils.safeValue(attribute.language()), isSystemGroup);
	}
}
