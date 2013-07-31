/**
 * 
 */
package org.cotrix.web.importwizard.server.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.Property;
import org.cotrix.web.importwizard.shared.RepositoryDetails;
import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
import org.virtualrepository.Properties;
import org.virtualrepository.RepositoryService;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Assets {
	
	public static AssetInfo convert(Asset asset)
	{
		AssetInfo assetInfo = new AssetInfo();
		
		assetInfo.setId(asset.id());
		assetInfo.setName(asset.name());
		assetInfo.setType(asset.type().toString());
		assetInfo.setRepositoryName(asset.service().name().toString());
		
		return assetInfo;
		
	}
	
	public static AssetDetails convertToDetails(Asset asset)
	{
		AssetDetails details = new AssetDetails();
		details.setId(asset.id());
		details.setName(asset.name());
		details.setProperties(convert(asset.properties()));
		details.setType(String.valueOf(asset.type()));
		details.setRepository(convert(asset.service()));
		return details;
	}
	
	protected static RepositoryDetails convert(RepositoryService service) {
		RepositoryDetails details = new RepositoryDetails();
		details.setName(service.name().toString());
		details.setProperties(convert(service.properties()));
		details.setPublishedTypes(convert(service.publishedTypes()));
		details.setReturnedTypes(convert(service.returnedTypes()));
		return details;
	}
	
	protected static String convert(Collection<AssetType> types)
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
	
	protected static List<Property> convert(Properties properties)
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

}
