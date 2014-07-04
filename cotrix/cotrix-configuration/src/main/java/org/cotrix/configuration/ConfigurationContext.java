/**
 * 
 */
package org.cotrix.configuration;

import static org.cotrix.common.CommonUtils.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
public class ConfigurationContext {

	//wraps a JAXB configured via CDI
	
	private JAXBContext context;

	
	@Inject
	public ConfigurationContext(Instance<ConfigurationBean> prototypes) {

		List<Class<?>> klasses = new ArrayList<Class<?>>();

		for (ConfigurationBean sample : prototypes)
			klasses.add(sample.getClass());

		try {
			
			context = JAXBContext.newInstance(klasses.toArray(new Class<?>[0]));

		} catch (Exception e) {

			rethrow("cannot create configuration context (see cause)", e);
		}
	}


	public List<ConfigurationBean> bind(InputStream stream) {

		try {

			Configuration container = (Configuration) context.createUnmarshaller().unmarshal(stream);

			return container.beans();
			
		}
		catch (JAXBException e) {
			throw unchecked("cannot read configuration (see cause)", e);
		}
	}

}
