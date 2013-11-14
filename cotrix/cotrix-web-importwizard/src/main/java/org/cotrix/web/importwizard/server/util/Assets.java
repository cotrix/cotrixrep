/**
 * 
 */
package org.cotrix.web.importwizard.server.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.Property;
import org.cotrix.web.importwizard.shared.RepositoryDetails;
import org.cotrix.web.share.server.util.AssetTypes;
import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
import org.virtualrepository.Properties;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.sdmx.SdmxCodelist;

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
		CodeListType codeListType = getCodeListType(asset.type());
		assetInfo.setCodeListType(codeListType);
		String serviceName = getServiceName(asset);
		assetInfo.setRepositoryId(serviceName);
		assetInfo.setRepositoryName(serviceName);
		
		return assetInfo;
		
	}
	
	protected static CodeListType getCodeListType(AssetType assetType)
	{
		if (assetType == SdmxCodelist.type) return CodeListType.SDMX;
		if (assetType == CsvCodelist.type) return CodeListType.CSV;
		throw new IllegalArgumentException("Unknow asset type "+assetType);
	}
	
	protected static String getServiceName(Asset asset)
	{
		RepositoryService service = asset.service();
		if (service == null) return null;
		QName name = service.name();
		if (name == null) return null;
		return String.valueOf(name);
	}
	
	public static AssetDetails convertToDetails(Asset asset)
	{
		AssetDetails details = new AssetDetails();
		details.setId(asset.id());
		details.setName(asset.name());
		details.setProperties(convert(asset.properties()));
		details.setType(String.valueOf(asset.type()));
		String serviceName = getServiceName(asset);
		details.setRepositoryName(serviceName);
		details.setRepositoryId(serviceName);
		return details;
	}
	
	public static RepositoryDetails convert(RepositoryService service) {
		RepositoryDetails details = new RepositoryDetails();
		details.setId(service.name().toString());
		details.setName(service.name().toString());
		details.setProperties(convert(service.properties()));
		details.setPublishedTypes(AssetTypes.toString(service.publishedTypes()));
		details.setReturnedTypes(AssetTypes.toString(service.returnedTypes()));
		return details;
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
