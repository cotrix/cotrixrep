/**
 * 
 */
package org.cotrix.web.manage.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.linktype.AttributeValue;
import org.cotrix.web.common.shared.codelist.linktype.LinkValue;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType.UIValueType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ValueTypesGrouper {
	
	private interface GroupKeyFunction<T extends UIValueType> {
		
		public String getGroupKey(T type);
		
		public GroupKeyFunction<T> nextFunction();
		
		public String prefix();
		public String suffix();
	}
	
	private enum AttributeTypeGroupKeyFunction implements GroupKeyFunction<AttributeValue> {
		NAME("","") {
			@Override
			public String getGroupKey(AttributeValue type) {
				return ValueUtils.getLocalPart(type.getName());
			}

			@Override
			public GroupKeyFunction<AttributeValue> nextFunction() {
				return LANGUAGE;
			}
		},
		LANGUAGE("[","]") {

			@Override
			public String getGroupKey(AttributeValue type) {
				return type.getLanguage();
			}
			

			@Override
			public GroupKeyFunction<AttributeValue> nextFunction() {
				return TYPE;
			}
		},
		TYPE("{","}") {

			@Override
			public String getGroupKey(AttributeValue type) {
				return ValueUtils.getLocalPart(type.getType());
			}

			@Override
			public GroupKeyFunction<AttributeValue> nextFunction() {
				return null;
			}
			
		};
		
		private String prefix;
		private String suffix;
		
		private AttributeTypeGroupKeyFunction(String prefix, String suffix) {
			this.prefix = prefix;
			this.suffix = suffix;
		}

		public String prefix() {
			return prefix;
		}

		public String suffix() {
			return suffix;
		}
	}
	
	private enum LinkTypeGroupKeyFunction implements GroupKeyFunction<LinkValue> {
			NAME("","") {
				@Override
				public String getGroupKey(LinkValue type) {
					return ValueUtils.getLocalPart(type.getName());
				}

				@Override
				public GroupKeyFunction<LinkValue> nextFunction() {
					return null;
				}
			}/*,
			LANGUAGE() {

				@Override
				public String getGroupKey(LinkType type) {
					return type.getLanguage();
				}
				

				@Override
				public GroupKeyFunction<LinkType> nextFunction() {
					return TYPE;
				}
			},
			TYPE() {

				@Override
				public String getGroupKey(LinkType type) {
					return ValueUtils.getLocalPart(type.getType());
				}

				@Override
				public GroupKeyFunction<LinkType> nextFunction() {
					return null;
				}
				
			}*/
		;	
		
		
		private String prefix;
		private String suffix;
		
		private LinkTypeGroupKeyFunction(String prefix, String suffix) {
			this.prefix = prefix;
			this.suffix = suffix;
		}

		public String prefix() {
			return prefix;
		}

		public String suffix() {
			return suffix;
		}
	}
	
	public static Map<LinkValue, String> generateLabelsForLinkTypes(List<LinkValue> linkTypes) {
		return generateLabels(linkTypes, LinkTypeGroupKeyFunction.NAME);
	}
	
	public static Map<AttributeValue, String> generateLabelsForAttributeTypes(List<AttributeValue> attributeTypes) {
		return generateLabels(attributeTypes, AttributeTypeGroupKeyFunction.NAME);
	}
	
	private static <T extends UIValueType> Map<T, String> generateLabels(List<T> attributeTypes, GroupKeyFunction<T> groupValueFunction) {
		
		Map<T, String> labels = new HashMap<T, String>();
		
		generateLabels(attributeTypes, groupValueFunction, labels, "");
		
		return labels;
	}
	
	private static <T extends UIValueType> void generateLabels(List<T> elements, GroupKeyFunction<T> groupKeyFunction, Map<T, String> labels, String labelPrefix) {
		Map<String, List<T>> groups = group(elements, groupKeyFunction);
		
		GroupKeyFunction<T> nextFunction = groupKeyFunction.nextFunction();
		
		for (Map.Entry<String, List<T>> group:groups.entrySet()) {
			String groupName = group.getKey();
			String name = labelPrefix + " " + groupKeyFunction.prefix() + groupName + groupKeyFunction.suffix();
			
			List<T> types = group.getValue();
			
			if (types.size()>1 && nextFunction != null) generateLabels(types, nextFunction, labels, name);
			else for (T type: types) labels.put(type, name);
		}
	}
	
	private static <T extends UIValueType> Map<String, List<T>> group(List<T> elements, GroupKeyFunction<T> groupKeyFunction) {
		Map<String, List<T>> groups = new HashMap<String, List<T>>();
		for (T element:elements) {
			String groupKey = groupKeyFunction.getGroupKey(element);
			List<T> group = groups.get(groupKey);
			if (group == null) {
				group = new ArrayList<T>();
				groups.put(groupKey, group);
			}
			group.add(element);
		}
		return groups;
	}

}
