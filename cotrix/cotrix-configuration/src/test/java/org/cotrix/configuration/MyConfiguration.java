/**
 * 
 */
package org.cotrix.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cotrix.common.Configuration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@XmlRootElement
public class MyConfiguration implements Configuration {
	
	@XmlElement
	protected String firstParameter;
	
	@XmlElement
	protected Integer secondParameter;
	
	public MyConfiguration(){}

	public MyConfiguration(String firstParameter, Integer secondParameter) {
		this.firstParameter = firstParameter;
		this.secondParameter = secondParameter;
	}	

}
