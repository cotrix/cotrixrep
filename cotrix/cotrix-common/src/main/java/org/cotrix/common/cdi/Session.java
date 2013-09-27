package org.cotrix.common.cdi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;


/**
 * An injectable application-level notion of session backed up at runtime by an HTTP session. 
 * @author Fabio Simeoni
 *
 */
@SuppressWarnings("serial")
@SessionScoped
public class Session implements Serializable {

	private final Map<String,Object> data = new HashMap<String, Object>();
	
	/**
	 * Returns the data in this session.
	 * 
	 * @return the data
	 */
	public Map<String, Object> data() {
		return data;
	}
	

}
