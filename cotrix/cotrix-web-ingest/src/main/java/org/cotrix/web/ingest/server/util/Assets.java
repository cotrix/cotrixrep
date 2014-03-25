/**
 * 
 */
package org.cotrix.web.ingest.server.util;

import org.cotrix.web.common.server.util.Repositories;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.ingest.shared.AssetDetails;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.ingest.shared.CodeListType;
import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
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
		UIQName serviceName = getServiceName(asset);
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
	
	protected static UIQName getServiceName(Asset asset)
	{
		RepositoryService service = asset.service();
		if (service == null) return null;
		return ValueUtils.safeValue(service.name());
	}
	
	public static AssetDetails convertToDetails(Asset asset)
	{
		AssetDetails details = new AssetDetails();
		details.setId(asset.id());
		details.setName(asset.name());
		details.setProperties(Repositories.convert(asset.properties()));
		details.setType(String.valueOf(asset.type()));
		UIQName serviceName = getServiceName(asset);
		details.setRepositoryName(serviceName);
		details.setRepositoryId(serviceName);
		return details;
	}

}
