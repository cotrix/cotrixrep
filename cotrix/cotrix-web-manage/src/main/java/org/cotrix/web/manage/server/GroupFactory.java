/**
 * 
 */
package org.cotrix.web.manage.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.utils.Constants;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.manage.shared.AttributeGroup;
import org.cotrix.web.manage.shared.Group;
import org.cotrix.web.manage.shared.HasPosition;
import org.cotrix.web.manage.shared.LinkGroup;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupFactory {
	
	private static final Comparator<Group> GROUP_COMPARATOR = new Comparator<Group>() {
		
		@Override
		public int compare(Group group1, Group group2) {
			if (group1 instanceof AttributeGroup && ((AttributeGroup)group1).isSystemGroup()) return 1;
			if (group2 instanceof AttributeGroup && ((AttributeGroup)group2).isSystemGroup()) return -1;
			
			int nameComparison = String.CASE_INSENSITIVE_ORDER.compare(group1.getName().getLocalPart(), group2.getName().getLocalPart());
			if (nameComparison!=0) return nameComparison;
			
			return Integer.compare(group1.getPosition(), group2.getPosition());
		}
	};
	
	
	private interface ItemProvider<T> {
		Iterable<? extends T> getItems(Code code);
	}
	
	private interface GroupGenerator<T, G extends Group> {
		G generate(T item);
	}

	private static final GroupGenerator<Attribute, AttributeGroup> ATTRIBUTE_GROUP_GENERATOR = new GroupGenerator<Attribute, AttributeGroup>() {

		@Override
		public AttributeGroup generate(Attribute attribute) {
			boolean isSystemGroup = isSystemAttribute(attribute);
			return new AttributeGroup(ValueUtils.safeValue(attribute.qname()), null, ValueUtils.safeLanguage(attribute.language()), isSystemGroup);
		}
	};
	
	private static final ItemProvider<Attribute> ATTRIBUTES_PROVIDER = new ItemProvider<Attribute>() {

		@Override
		public Iterable<? extends Attribute> getItems(Code code) {
			return filter(code.attributes());
		}
		
		private Iterable<? extends Attribute> filter(Iterable<? extends Attribute> attributes) {
			List<Attribute> filtered = new ArrayList<>();
			for (Attribute attribute:attributes) if (!isSystemAttribute(attribute)) filtered.add(attribute);
			return filtered;
		}
	};
	
	private static final GroupGenerator<Link, LinkGroup> LINK_GROUP_GENERATOR = new GroupGenerator<Link, LinkGroup>() {

		@Override
		public LinkGroup generate(Link link) {
			boolean isSystemGroup = link.definition()!=null?link.definition().equals(Constants.SYSTEM_TYPE):false;
			return new LinkGroup(ValueUtils.safeValue(link.qname()), isSystemGroup);
		}
	};
	
	private static final ItemProvider<Link> CODELINKS_PROVIDER = new ItemProvider<Link>() {

		@Override
		public Iterable<? extends Link> getItems(Code code) {
			return code.links();
		}
	};
	
	public static List<Group> getGroups(Iterable<Code> codes) {
		List<Group> groups = getGroups(codes, ATTRIBUTES_PROVIDER, ATTRIBUTE_GROUP_GENERATOR);
		groups.addAll(getGroups(codes, CODELINKS_PROVIDER, LINK_GROUP_GENERATOR));
		
		Collections.sort(groups, GROUP_COMPARATOR);
		return groups;
		
	}
	
	private static boolean isSystemAttribute(Attribute attribute) {
		return attribute.type()!=null?attribute.is(Constants.SYSTEM_TYPE):false;
	}
		
	@SuppressWarnings("unchecked")
	private static <T, G extends Group & HasPosition> List<Group> getGroups(Iterable<Code> codes, ItemProvider<T> itemProvider, GroupGenerator<T, G> groupGenerator)
	{
		Map<G, Integer> groupsCounter = new HashMap<G, Integer>();
		for (Code code:codes) {
			Map<G, Integer> itemGroupsCounter = new HashMap<G, Integer>();
			
			for (T item:itemProvider.getItems(code)) {
				G group = groupGenerator.generate(item);
				
				Integer counter = itemGroupsCounter.get(group);
				if (counter == null) counter = 0;
				counter++;
				itemGroupsCounter.put(group, counter);
			}
			for (Entry<G, Integer> itemGroupCounter:itemGroupsCounter.entrySet()) {
				Integer groupCounter = groupsCounter.get(itemGroupCounter.getKey());
				groupCounter = groupCounter == null?0:groupCounter;
				int count = Math.max(groupCounter, itemGroupCounter.getValue());
				groupsCounter.put(itemGroupCounter.getKey(), count);
			}
		}
		
		List<Group> groups = new ArrayList<Group>();
		for (Entry<G, Integer> groupCounter:groupsCounter.entrySet()) {
			for (int i = 0; i < groupCounter.getValue(); i++) {
				G group = (G) ((G) groupCounter.getKey()).cloneGroup();
				group.setPosition(i);
				groups.add(group);
			}
		}

		return groups;
	}

}
