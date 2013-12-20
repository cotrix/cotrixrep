/**
 * 
 */
package org.cotrix.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cotrix.common.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@XmlRootElement(name="cotrix")
@XmlAccessorType(XmlAccessType.FIELD)
public class CotrixConfiguration {
	
	private static Logger log = LoggerFactory.getLogger(CotrixConfiguration.class);
	
    @XmlAnyElement(lax=true)
	protected List<Configuration> configurations = new ArrayList<Configuration>();
    
    public CotrixConfiguration(){}

	/**
	 * @param configurations
	 */
	public CotrixConfiguration(List<Configuration> configurations) {
		this.configurations = configurations;
	}

	/**
	 * @return the configurations
	 */
	public List<Configuration> getConfigurations() {
		return configurations;
	}

	
	//JAXB callback: remove unbound configuration elements
	public void afterUnmarshal(Unmarshaller u, Object parent) throws UnmarshalException {
					
		Iterator<Configuration> it = configurations.iterator();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof Element) {
	    		log.info("discarding unbound configuration: "+Element.class.cast(next).getNodeName());
	    		it.remove();
			}
		}
	}
	
}
