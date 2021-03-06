/**
 * 
 */
package org.cotrix.web.manage.client.di;

import org.cotrix.web.manage.client.CotrixManageGinInjector;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class AttributeTypesCacheProvider implements Provider<AttributeDefinitionsCache> {

	protected AttributeDefinitionsCache cache;

	public void generate() {
		this.cache = CotrixManageGinInjector.INSTANCE.getAttributeTypesCache();
	}

	@Override
	public AttributeDefinitionsCache get() {
		return cache;
	}

}
