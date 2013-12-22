package org.cotrix.neo;

import static org.cotrix.common.Utils.*;

import java.io.File;
import java.util.Map;

import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cotrix.configuration.ConfigurationBean;
import org.cotrix.configuration.utils.Attributes;

@XmlRootElement(name = "neo")
public class NeoConfiguration implements ConfigurationBean {

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
			
			try {
				
				File touch = File.createTempFile("cotrix-", ".neo");
				location = touch.getParentFile().getAbsolutePath();
				touch.delete();
			}
			catch(Exception e) {
				
				rethrow("cannot set temporary location for Neo database (see cause)",e);
			}
		
		else 
			valid(new File(location));

	}
}
