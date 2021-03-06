/**
 * 
 */
package org.cotrix.web.common.shared.exception;

import org.cotrix.web.common.shared.Error;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Exceptions {
	
	public static ServiceErrorException toServiceErrorException(String errorMessage, Throwable throwable) {
		return new ServiceErrorException(toError(errorMessage, throwable));
	}
	
	public static ServiceErrorException toServiceErrorException(Throwable throwable) {
		return new ServiceErrorException(toError(throwable));
	}
	
	public static Error toError(String errorMessage, Throwable throwable) {
		if (throwable instanceof ServiceErrorException) return new Error(errorMessage, ((ServiceErrorException)throwable).getError().getDetails());
		String details = Exceptions.getPrintStackTrace(throwable);
		return new Error(errorMessage, details);
	}

	public static Error toError(Throwable throwable) {
		if (throwable instanceof ServiceErrorException) return ((ServiceErrorException)throwable).getError();
		String details = Exceptions.getPrintStackTrace(throwable);
		String errorMessage = getErrorMessage(throwable);
		return new Error(errorMessage, details);
	}

	private static String getErrorMessage(Throwable throwable) {
		if (throwable instanceof IncompatibleRemoteServiceException) return "Looks like you're running an old version of Cotrix, please <a href=\"http://en.wikipedia.org/wiki/Wikipedia:Bypass_your_cache\" target=\"_blank\">hard refresh</a> your browser";
		if (throwable instanceof InvocationException) return "Uhm, we cannot reach our servers. Check your connection or try again in a little bit.";
		if (throwable instanceof ServiceException) return "Ooops an error occurred on server side";

		return "Ooops an error occurred on server side";
	}

	private static String getPrintStackTrace(Throwable throwable) {
		StringBuilder builder = new StringBuilder();
		printStackTrace(throwable, builder);
		return builder.toString();
	}

	protected static void printStackTrace(Throwable throwable, StringBuilder s) {
		s.append(throwable.toString());
		StackTraceElement[] trace = throwable.getStackTrace();
		for (int i=0; i < trace.length; i++)
			s.append("&nbsp;&nbsp;at " + trace[i]+"<br>");

		Throwable ourCause = throwable.getCause();
		if (ourCause != null)
			printStackTraceAsCause(ourCause, s, trace);
	}

	/**
	 * Print our stack trace as a cause for the specified stack trace.
	 */
	private static void printStackTraceAsCause(Throwable throwable, StringBuilder s,
			StackTraceElement[] causedTrace)
	{

		// Compute number of frames in common between this and caused
		StackTraceElement[] trace = throwable.getStackTrace();
		int m = trace.length-1, n = causedTrace.length-1;
		while (m >= 0 && n >=0 && trace[m].equals(causedTrace[n])) {
			m--; n--;
		}
		int framesInCommon = trace.length - 1 - m;

		s.append("<br>Caused by: " + throwable.toString()+"<br>");
		for (int i=0; i <= m; i++)
			s.append("&nbsp;&nbsp;at " + trace[i]+"<br>");
		if (framesInCommon != 0)
			s.append("&nbsp;&nbsp;... " + framesInCommon + " more<br>");

		// Recurse if we have a cause
		Throwable ourCause = throwable.getCause();
		if (ourCause != null)
			printStackTraceAsCause(ourCause, s, trace);
	}

}
