package org.cotrix.neo;

import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cotrix.common.Constants;
import org.cotrix.common.cdi.Current;
import org.cotrix.configuration.ConfigurationBean;
import org.cotrix.configuration.Provider;
import org.cotrix.configuration.utils.Attributes;

@XmlRootElement(name = "neo")
public class NeoConfiguration implements ConfigurationBean {

	@Produces @Singleton @Current
	static NeoConfiguration produce(Provider<NeoConfiguration> provider) {
		return provider.get();
	}
	
	@XmlElement
	private String location;

	@XmlElement
	private Attributes properties = new Attributes();
	
	public String location() {
		return location;
	}
	
	public Map<String,String> properties() {
		return properties.asMap();
	}

	// JAXB callback: set defaults and validate
	public void afterUnmarshal(Unmarshaller u, Object parent) throws UnmarshalException {

		if (location==null)
			location = Constants.DEFAULT_STORAGE_DIR;
		
	}
}
