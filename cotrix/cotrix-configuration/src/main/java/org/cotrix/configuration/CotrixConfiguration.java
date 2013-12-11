/**
 * 
 */
package org.cotrix.configuration;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cotrix.common.Configuration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@XmlRootElement(name="cotrix")
@XmlAccessorType(XmlAccessType.FIELD)
public class CotrixConfiguration {
	
    @XmlAnyElement(lax=true)
	protected List<Configuration> configurations;
    
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

}
