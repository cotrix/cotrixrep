/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cotrix.web.common.shared.codelist.Property;
import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.virtualrepository.AssetType;
import org.virtualrepository.Properties;
import org.virtualrepository.RepositoryService;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Repositories {
	
	public static RepositoryDetails convert(RepositoryService service) {
		RepositoryDetails details = new RepositoryDetails();
		details.setId(ValueUtils.safeValue(service.name()));
		details.setName(ValueUtils.safeValue(service.name()));
		details.setProperties(convert(service.properties()));
		details.setPublishedTypes(Repositories.toString(service.publishedTypes()));
		details.setReturnedTypes(Repositories.toString(service.returnedTypes()));
		return details;
	}

	public static List<Property> convert(Properties properties)
	{
		List<Property> props = new ArrayList<Property>();
		for (org.virtualrepository.Property property:properties) {
			if (property.isDisplay()) props.add(convert(property));
		}
		
		return props;
	}
	
	protected static Property convert(org.virtualrepository.Property property)
	{
		return new Property(property.name(), String.valueOf(property.value()), property.description());
	}
	
	public static String toString(Collection<AssetType> types)
	{
		if (types.isEmpty()) return "";
		StringBuilder builder = new StringBuilder();
		Iterator<AssetType> iterator = types.iterator();
		
		while(iterator.hasNext()) {
			builder.append(String.valueOf(iterator.next()));
			if (iterator.hasNext()) builder.append(", ");
		}
		return builder.toString();
	}

}
