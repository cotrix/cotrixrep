/**
 * 
 */
package org.cotrix.web.share.server.util;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ExceptionUtils {
	
	public static <T extends Throwable> T unfoldException(Throwable exception, Class<T> exptectedExceptionClass) {
		if (exception == null) return null;
		if (exception.getClass().equals(exptectedExceptionClass)) return exptectedExceptionClass.cast(exception);
		return unfoldException(exception.getCause(), exptectedExceptionClass);
		
	}

}
