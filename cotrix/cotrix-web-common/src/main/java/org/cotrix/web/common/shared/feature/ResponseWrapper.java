/**
 * 
 */
package org.cotrix.web.common.shared.feature;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ResponseWrapper<T> extends AbstractFeatureCarrier implements IsSerializable {
	
	public static <T> ResponseWrapper<T> wrap(T payload)
	{
		return new ResponseWrapper<T>(payload);
	}
	
	protected T payload;
	
	protected ResponseWrapper(){}

	/**
	 * @param payload
	 */
	public ResponseWrapper(T payload) {
		this.payload = payload;
	}

	/**
	 * @return the payload
	 */
	public T getPayload() {
		return payload;
	}
}
