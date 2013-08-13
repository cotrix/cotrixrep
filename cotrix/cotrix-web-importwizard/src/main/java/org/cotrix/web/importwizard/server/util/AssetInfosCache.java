/**
 * 
 */
package org.cotrix.web.importwizard.server.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.cotrix.io.Channels;
import org.cotrix.web.importwizard.server.util.FieldComparator.ValueProvider;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetInfosCache {
	
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
	
	public static final String SESSION_ATTRIBUTE_NAME = AssetInfosCache.class.getSimpleName();
	
	public static AssetInfosCache getFromSession(HttpSession httpSession, VirtualRepository remoteRepository){
		AssetInfosCache assetCache = (AssetInfosCache) httpSession.getAttribute(SESSION_ATTRIBUTE_NAME);
		if (assetCache == null) {
			assetCache = new AssetInfosCache(remoteRepository);
			httpSession.setAttribute(SESSION_ATTRIBUTE_NAME, assetCache);
		}
		return assetCache;
	}
	
	protected Logger logger = LoggerFactory.getLogger(AssetInfosCache.class);
	
	protected VirtualRepository remoteRepository;
	
	protected Map<String, Asset> assetsCache;
	protected OrderedList<AssetInfo> cache;
	protected boolean cacheLoaded = false;
	
	/**
	 * @param remoteRepository
	 */
	public AssetInfosCache(VirtualRepository remoteRepository) {
		this.remoteRepository = remoteRepository;
		setupCache();
	}

	protected void setupCache()
	{
		cache = new OrderedList<AssetInfo>();
		cache.addField(AssetInfo.NAME_FIELD, NAME_PROVIDER);
		cache.addField(AssetInfo.REPOSITORY_FIELD, REPOSITORY_PROVIDER);
		
		assetsCache = new HashMap<String, Asset>();
	}
	
	public void refreshCache()
	{
		int discovered = remoteRepository.discover(Channels.importTypes);
		logger.trace("discovered "+discovered+" remote codelist");
		loadCache();
	}
	
	public void loadCache()
	{
		cache.clear();
		assetsCache.clear();

		for (Asset asset:remoteRepository) {
	
			AssetInfo assetInfo = Assets.convert(asset);
			logger.trace("converted {} to {}", asset.name(), assetInfo);
			cache.add(assetInfo);
			assetsCache.put(asset.id(), asset);
		}
		
		cache.sort();
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
	
	public List<AssetInfo> getAssets(String field)
	{
		ensureCacheInitialized();
		if (field == null) return cache.getSortedList(AssetInfo.NAME_FIELD);
		return cache.getSortedList(field);
	}


}
