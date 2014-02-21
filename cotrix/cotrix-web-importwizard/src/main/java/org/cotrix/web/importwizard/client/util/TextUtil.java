/**
 * 
 */
package org.cotrix.web.importwizard.client.util;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TextUtil {
	
	private final static String NON_THIN = "[^iIl1\\.,']";
	protected static NumberFormat format = NumberFormat.getDecimalFormat();

	private static int textWidth(String str) {
	    return (int) (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
	}

	public static String ellipsize(String text, int max) {

	    if (textWidth(text) <= max)
	        return text;

	    // Start by chopping off at the word before max
	    // This is an over-approximation due to thin-characters...
	    int end = text.lastIndexOf(' ', max - 3);

	    // Just one long word. Chop it off.
	    if (end == -1)
	        return text.substring(0, max-3) + "...";

	    // Step forward as long as textWidth allows.
	    int newEnd = end;
	    do {
	        end = newEnd;
	        newEnd = text.indexOf(' ', end + 1);

	        // No more spaces.
	        if (newEnd == -1)
	            newEnd = text.length();

	    } while (textWidth(text.substring(0, newEnd) + "...") < max);

	    return text.substring(0, end) + "...";
	}
	
	public static String toKiloBytes(long bytes)
	{
		long size = Math.max(1, bytes/1000);
		return format.format(size);
	}

}
