/**
 * 
 */
package org.cotrix.web.share.shared.feature;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Request<T> implements IsSerializable {
	
	public static Request<Void> voidRequest(String codelistId)
	{
		return new Request<Void>(codelistId, null);
	}
	
	protected String id;
	protected T input;
	
	public Request(){}
	
	/**
	 * @param id
	 * @param input
	 */
	public Request(String id, T input) {
		this.id = id;
		this.input = input;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the input
	 */
	public T getInput() {
		return input;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Request [id=");
		builder.append(id);
		builder.append(", input=");
		builder.append(input);
		builder.append("]");
		return builder.toString();
	}
	
	
}
