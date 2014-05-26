/**
 * 
 */
package org.cotrix.web.manage.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistRemoveCheckResponse implements IsSerializable {

	private boolean canRemove;

	private String cause;
	
	public static CodelistRemoveCheckResponse cannot(String cause) {
		return new CodelistRemoveCheckResponse(false, cause);
	}
	
	public static CodelistRemoveCheckResponse can() {
		return new CodelistRemoveCheckResponse(true, null);
	}

	private CodelistRemoveCheckResponse() {}

	private CodelistRemoveCheckResponse(boolean canRemove, String cause) {
		this.canRemove = canRemove;
		this.cause = cause;
	}

	public String getCause() {
		return cause;
	}


	public boolean isCanRemove() {
		return canRemove;
	}


	public void setCanDelete(boolean canDelete) {
		this.canRemove = canDelete;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodelistRemoveCheckResponse [canRemove=");
		builder.append(canRemove);
		builder.append(", cause=");
		builder.append(cause);
		builder.append("]");
		return builder.toString();
	}


}
