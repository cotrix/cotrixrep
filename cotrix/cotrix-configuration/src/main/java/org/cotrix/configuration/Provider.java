package org.cotrix.configuration;

import javax.enterprise.inject.Vetoed;



/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Vetoed
public class Provider<T extends ConfigurationBean> {

	protected T configuration;

	public Provider(T configuration) {
		this.configuration = configuration;
	}

	public T get() {
		return configuration;
	}
}
