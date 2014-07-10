/**
 * 
 */
package org.cotrix.web.common.shared.async;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AsyncTask<T extends IsSerializable> implements AsyncOutput<T> {
	
	private String id;

	protected AsyncTask() {}
	
	public AsyncTask(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AsyncTask [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
