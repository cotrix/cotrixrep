/**
 * 
 */
package org.cotrix.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@XmlRootElement(name="cotrix")
public class Configuration implements ConfigurationBean {
	
	private static Logger log = LoggerFactory.getLogger(Configuration.class);
	
   
	@XmlAnyElement(lax=true)
	protected List<ConfigurationBean> beans = new ArrayList<ConfigurationBean>();
    
    //for JAXB
    Configuration(){}

    
	public List<ConfigurationBean> beans() {
		return beans;
	}

	
	//JAXB callback: remove unbound configuration elements
	public void afterUnmarshal(Unmarshaller u, Object parent) throws UnmarshalException {
					
		Iterator<ConfigurationBean> it = beans.iterator();
		
		while (it.hasNext()) {
			
			Object next = it.next();
			
			if (next instanceof Element) {
	    		
				log.info("discarding unbound configuration: "+Element.class.cast(next).getNodeName());
	    		
	    		it.remove();
			}
		}
	}
	
}
