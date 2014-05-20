/**
 * 
 */
package org.cotrix.web.common.client.util;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Exceptions {
	
	public static String getPrintStackTrace(Throwable throwable) {
		StringBuilder builder = new StringBuilder();
		printStackTrace(throwable, builder);
		return builder.toString();
	}

	protected static void printStackTrace(Throwable throwable, StringBuilder s) {
			s.append(throwable.toString());
			StackTraceElement[] trace = throwable.getStackTrace();
			for (int i=0; i < trace.length; i++)
				s.append("\tat " + trace[i]);

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

		s.append("Caused by: " + throwable.toString());
		for (int i=0; i <= m; i++)
			s.append("\tat " + trace[i]);
		if (framesInCommon != 0)
			s.append("\t... " + framesInCommon + " more");

		// Recurse if we have a cause
		Throwable ourCause = throwable.getCause();
		if (ourCause != null)
			printStackTraceAsCause(ourCause, s, trace);
	}

}
