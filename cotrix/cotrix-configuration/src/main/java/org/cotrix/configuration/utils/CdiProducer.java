/**
 * 
 */
package org.cotrix.configuration.utils;

import java.lang.reflect.ParameterizedType;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.configuration.ConfigurationBean;
import org.cotrix.configuration.ConfigurationManager;
import org.cotrix.configuration.Provider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
@Singleton
public class CdiProducer {

	@Inject
	private ConfigurationManager manager;

	@Produces
	public <T extends ConfigurationBean> Provider<T> produce(InjectionPoint ip) {

		ParameterizedType ptype = (ParameterizedType) ip.getType();

		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) ptype.getActualTypeArguments()[0];  
		
		return new Provider<T>(manager.get(type));
		
	}
}
