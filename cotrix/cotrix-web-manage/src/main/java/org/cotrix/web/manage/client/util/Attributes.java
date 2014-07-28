/**
 * 
 */
package org.cotrix.web.manage.client.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.cotrix.web.common.client.factory.UIDefaults;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Attributes {
	
	@Inject
	public static UIDefaults defaults;
	
	protected static final Comparator<UIAttribute> comparator = new Comparator<UIAttribute>() {
		
		@Override
		public int compare(UIAttribute o1, UIAttribute o2) {
			if (isSystemAttribute(o1)) return 1;
			if (isSystemAttribute(o2)) return -1;
			return String.CASE_INSENSITIVE_ORDER.compare(o1.getName().getLocalPart(), o2.getName().getLocalPart());
		}
	};
	
	public static boolean isSystemAttribute(UIAttribute attribute) {
		if (attribute == null) return false;
		return isSystemType(attribute.getType());
	}
	
	protected static boolean isSystemType(UIQName type)
	{
		if (type == null) return false;
		return defaults.systemType().equals(type);
	}
	
	public static void sortByAttributeType(List<UIAttribute> attributes) {
		Collections.sort(attributes, comparator);
	}
	
	public static void mergeSystemAttributes(List<UIAttribute> destination, List<UIAttribute> source) {
		//update and add
		for (UIAttribute sourceAttribute:source) {
			if (Attributes.isSystemAttribute(sourceAttribute)) {
				int index = destination.indexOf(sourceAttribute);
				if (index >= 0) destination.set(index, sourceAttribute);
				else destination.add(sourceAttribute);
			}
		}
		
		//remotion
		Iterator<UIAttribute> destinationIterator = destination.iterator();
		while(destinationIterator.hasNext()) {
			UIAttribute attribute = destinationIterator.next();
			if (Attributes.isSystemAttribute(attribute) && !source.contains(attribute)) destinationIterator.remove();
		}
	}

}
