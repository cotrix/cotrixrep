/**
 * 
 */
package org.cotrix.web.manage.client.di;

import org.cotrix.web.manage.client.CotrixManageGinInjector;
import org.cotrix.web.manage.client.codelist.cache.LinkTypesCache;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class LinkTypesCacheProvider implements Provider<LinkTypesCache> {

	protected LinkTypesCache cache;

	public void generate() {
		this.cache = CotrixManageGinInjector.INSTANCE.getLinkTypesCache();
	}

	@Override
	public LinkTypesCache get() {
		return cache;
	}

}
