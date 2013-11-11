/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist.attribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.cotrix.web.share.shared.codelist.UIAttribute;
import org.cotrix.web.share.shared.codelist.UICode;
import org.cotrix.web.share.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupFactory {
	
	public static final AttributeField[] GROUP_MASK = new AttributeField[]{AttributeField.NAME, AttributeField.LANGUAGE};
	
	/*public static Set<Group> getGroups(List<UICodelistRow> rows)
	{
		Set<Group> groups = new HashSet<Group>();
		for (UICodelistRow row:rows) getGroups(row, groups); 
		return groups;
	}
	
	protected static void getGroups(UICodelistRow row, Set<Group> groups)
	{
		getGroups(row.getAttributes(), groups);
	}
	
	protected static void getGroups(List<UIAttribute> attributes, Set<Group> groups)
	{
		for (UIAttribute attribute:attributes) {
			boolean accept = accept(groups, attribute, attributes);
			if (accept) continue;
			Group group = getGroup(attribute, GROUP_MASK);
			group.calculatePosition(attributes, attribute);
			groups.add(group);
		}
	}
	
	protected static boolean accept(Set<Group> groups, UIAttribute attribute, List<UIAttribute> attributes)
	{
		for (Group group:groups) if (group.accept(attributes, attribute)) return true;
		return false;
	}*/
	
	public static Set<Group> getGroups(List<UICode> rows)
	{
		Map<Group, Integer> groupsCounter = new HashMap<Group, Integer>();
		for (UICode row:rows) {
			Map<Group, Integer> attributeGroupsCounter = new HashMap<Group, Integer>();
			for (UIAttribute attribute:row.getAttributes()) {
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
	
	public static Group getGroup(UIAttribute attribute)
	{
		return new Group(attribute.getName(), null, attribute.getLanguage());
	}
	
	public static Group getGroup(UIAttribute attribute, AttributeField ... attributeFields)
	{
		UIQName name = contains(AttributeField.NAME, attributeFields)?attribute.getName():null;
		UIQName type = contains(AttributeField.TYPE, attributeFields)?attribute.getType():null;
		String language = contains(AttributeField.LANGUAGE, attributeFields)?attribute.getLanguage():null;
		Group group = new Group(name, type, language);
		return group;
	}
	
	protected static boolean contains(AttributeField field, AttributeField[] fields)
	{
		if (fields == null || fields.length == 0) return false;
		for (AttributeField attributeField:fields) if (attributeField == field) return true;
		return false;
	}

}
