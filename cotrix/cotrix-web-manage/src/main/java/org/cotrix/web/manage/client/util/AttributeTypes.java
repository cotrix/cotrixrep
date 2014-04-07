/**
 * 
 */
package org.cotrix.web.manage.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.link.AttributeType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypes {
	
	private enum GroupValueFunction {
			NAME() {
				@Override
				public String getGroupValue(AttributeType type) {
					return ValueUtils.getLocalPart(type.getName());
				}

				@Override
				public GroupValueFunction nextFunction() {
					return LANGUAGE;
				}
			},
			LANGUAGE() {

				@Override
				public String getGroupValue(AttributeType type) {
					return type.getLanguage();
				}
				

				@Override
				public GroupValueFunction nextFunction() {
					return TYPE;
				}
			},
			TYPE() {

				@Override
				public String getGroupValue(AttributeType type) {
					return ValueUtils.getLocalPart(type.getType());
				}

				@Override
				public GroupValueFunction nextFunction() {
					return null;
				}
				
			}
		;
		public abstract String getGroupValue(AttributeType type);
		
		public abstract GroupValueFunction nextFunction();
		
	}
	
	//<name> [<lang>(,type=<type>)?]?
	public static Map<AttributeType, String> generateLabels(List<AttributeType> attributeTypes) {
		
		Map<AttributeType, String> labels = new HashMap<AttributeType, String>();
		
		generateLabels(attributeTypes, GroupValueFunction.NAME, labels, "");
		
		return labels;
	}
	
	protected static void generateLabels(List<AttributeType> attributeTypes, GroupValueFunction groupValueFunction, Map<AttributeType, String> labels, String labelPrefix) {
		Map<String, List<AttributeType>> groupped = group(attributeTypes, groupValueFunction);
		
		GroupValueFunction nextFunction = groupValueFunction.nextFunction();
		
		for (Map.Entry<String, List<AttributeType>> group:groupped.entrySet()) {
			String groupName = group.getKey();
			String name = labelPrefix + " " + groupName;
			
			List<AttributeType> types = group.getValue();
			
			if (types.size()>1 && nextFunction != null) generateLabels(types, nextFunction, labels, name);
			else for (AttributeType type: types) labels.put(type, name);
		}
	}
	
	protected static Map<String, List<AttributeType>> group(List<AttributeType> attributeTypes, GroupValueFunction groupValueFunction) {
		Map<String, List<AttributeType>> groups = new HashMap<String, List<AttributeType>>();
		for (AttributeType attributeType:attributeTypes) {
			String groupValue = groupValueFunction.getGroupValue(attributeType);
			List<AttributeType> group = groups.get(groupValue);
			if (group == null) {
				group = new ArrayList<AttributeType>();
				groups.put(groupValue, group);
			}
			group.add(attributeType);
		}
		return groups;
	}

}
