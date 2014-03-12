/**
 * 
 */
package org.cotrix.web.ingest.server.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.cotrix.io.CloudService;
import org.cotrix.web.common.server.util.OrderedLists;
import org.cotrix.web.common.server.util.Repositories;
import org.cotrix.web.common.server.util.OrderedLists.ValueProvider;
import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SessionScoped
public class AssetInfosCache implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5918630106298363538L;

	protected static final ValueProvider<AssetInfo> NAME_PROVIDER = new ValueProvider<AssetInfo>() {

		@Override
		public String getValue(AssetInfo item) {
			return item.getName();
		}
	};
	
	protected static final ValueProvider<AssetInfo> REPOSITORY_PROVIDER = new ValueProvider<AssetInfo>() {

		@Override
		public String getValue(AssetInfo item) {
			return item.getRepositoryName();
		}
	};
	
	protected Logger logger = LoggerFactory.getLogger(AssetInfosCache.class);
	
	@Inject
	transient protected CloudService cloud;
	
	protected Map<String, Asset> assetsCache;
	protected Map<String, RepositoryDetails> repositoriesCache;
	protected OrderedLists<AssetInfo> cache;
	protected boolean cacheLoaded = false;
	
	/**
	 * @param cloud
	 */
	public AssetInfosCache() {
		setupCache();
	}

	protected void setupCache()
	{
		cache = new OrderedLists<AssetInfo>();
		cache.addField(AssetInfo.NAME_FIELD, NAME_PROVIDER);
		cache.addField(AssetInfo.REPOSITORY_FIELD, REPOSITORY_PROVIDER);
		
		assetsCache = new HashMap<String, Asset>();
		repositoriesCache = new HashMap<String, RepositoryDetails>();
	}
	
	public void refreshCache()
	{
		cloud.discover();
		loadCache();
	}
	
	public void loadCache()
	{
		cache.clear();
		assetsCache.clear();
		repositoriesCache.clear();

		for (Asset asset:cloud) {
	
			AssetInfo assetInfo = Assets.convert(asset);
			logger.trace("converted {} to {}", asset.name(), assetInfo);
			cache.add(assetInfo);
			assetsCache.put(asset.id(), asset);
		}
		
		for (RepositoryService repository: cloud.repositories()) {
			RepositoryDetails repositoryDetails = Repositories.convert(repository);
			repositoriesCache.put(repositoryDetails.getName(), repositoryDetails);
		}
	}
	
	protected void ensureCacheInitialized()
	{
		if (!cacheLoaded) {
			loadCache();
			cacheLoaded = true;
		}
	}
	
	public Asset getAsset(String id)
	{
		ensureCacheInitialized();
		return assetsCache.get(id);
	}
	
	public RepositoryDetails getRepository(String id)
	{
		ensureCacheInitialized();
		return repositoriesCache.get(id);
	}
	
	public List<AssetInfo> getAssets(String field)
	{
		ensureCacheInitialized();
		if (field == null) return cache.getSortedList(AssetInfo.NAME_FIELD);
		return cache.getSortedList(field);
	}


}
