package org.cotrix.common.async;

@SuppressWarnings("serial")
public class CancelledTaskException extends RuntimeException {

	public CancelledTaskException(String msg) {
		super(msg);
	}
}
