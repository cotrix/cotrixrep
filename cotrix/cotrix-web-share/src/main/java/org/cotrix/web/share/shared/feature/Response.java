/**
 * 
 */
package org.cotrix.web.share.shared.feature;

import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Response<T> implements IsSerializable {
	
	public static <T> Response<T> wrap(T payload)
	{
		return new Response<T>(payload);
	}
	
	protected Set<UIFeature> applicationFeatures;
	protected Map<String, Set<UIFeature>> codelistsFeatures;
	protected T payload;
	
	public Response(){}
	
	/**
	 * @param applicationFeatures
	 * @param payload
	 */
	public Response(T payload) {
		this.payload = payload;
	}
	
	/**
	 * @param applicationFeatures
	 * @param payload
	 */
	public Response(Set<UIFeature> applicationFeatures, T payload) {
		this.applicationFeatures = applicationFeatures;
		this.payload = payload;
	}

	/**
	 * @return the features
	 */
	public Set<UIFeature> getApplicationFeatures() {
		return applicationFeatures;
	}

	/**
	 * @param applicationFeatures the features to set
	 */
	public void setApplicationFeatures(Set<UIFeature> applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	/**
	 * @return the codelistsFeatures
	 */
	public Map<String, Set<UIFeature>> getCodelistsFeatures() {
		return codelistsFeatures;
	}

	/**
	 * @param codelistsFeatures the codelistsFeatures to set
	 */
	public void setCodelistsFeatures(Map<String, Set<UIFeature>> codelistsFeatures) {
		this.codelistsFeatures = codelistsFeatures;
	}

	/**
	 * @return the payload
	 */
	public T getPayload() {
		return payload;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Response [applicationFeatures=");
		builder.append(applicationFeatures);
		builder.append(", codelistsFeatures=");
		builder.append(codelistsFeatures);
		builder.append(", payload=");
		builder.append(payload);
		builder.append("]");
		return builder.toString();
	}	
}
