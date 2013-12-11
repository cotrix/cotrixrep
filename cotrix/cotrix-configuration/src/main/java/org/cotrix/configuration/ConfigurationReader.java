/**
 * 
 */
package org.cotrix.configuration;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.cotrix.common.Configuration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ConfigurationReader {
	
	protected JAXBContext context;
	protected Unmarshaller unmarshaller;
	protected Marshaller marshaller;
	
	public ConfigurationReader(Iterator<Class<? extends Configuration>> configurations) {
		try {
			ArrayList<Class<?>> classtobind = new ArrayList<Class<?>>();
			while(configurations.hasNext()) classtobind.add(configurations.next());
			classtobind.add(CotrixConfiguration.class);
			context = JAXBContext.newInstance(classtobind.toArray(new Class[classtobind.size()]));
			unmarshaller = context.createUnmarshaller();
		} catch (Exception e) {
			throw new RuntimeException("Unmarshaller creation failed", e);
		}
	}
	
	public List<Configuration> readFromStream(InputStream stream) {
		try {
			CotrixConfiguration container = (CotrixConfiguration) unmarshaller.unmarshal(stream);
			List<Configuration> configurations = container.getConfigurations();
			return (configurations!=null)?configurations:Collections.<Configuration>emptyList();
		} catch (JAXBException e) {
			throw new RuntimeException("Unmarshalling from stream failed", e);
		}
	}
	
	/**
	 * Lazy creator of the {@link Marshaller}.
	 * @return the marshaller.
	 */
	protected Marshaller getMarshaller()
	{
		if (marshaller == null) {
			try {
				marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			} catch (Exception e) {
				throw new RuntimeException("Unmarshaller creation failed", e);
			}
		}
		return marshaller;
	}
	
	public String write(List<Configuration> configurations) {
		try {
			StringWriter writer = new StringWriter();
			Marshaller marshaller = getMarshaller();
			
			CotrixConfiguration wrapper = new CotrixConfiguration(configurations);
			
			marshaller.marshal(wrapper, writer);
			return writer.toString();
		} catch(Exception e) {
			throw new RuntimeException("Failed configurations serialization", e);
		}
	}

}
