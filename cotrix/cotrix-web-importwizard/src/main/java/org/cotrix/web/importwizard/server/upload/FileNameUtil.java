/**
 * 
 */
package org.cotrix.web.importwizard.server.upload;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FileNameUtil {
	
	protected static final String[] suffixes = {".txt", ".csv"};
	
	public static String toHumanReadable(String filename)
	{
		if (filename == null) return null;
		
		filename = cleanSuffixes(filename);
		
		//underscore
		filename = filename.replaceAll("_", " ");
		
		return filename;
	}
	
	protected static String cleanSuffixes(String filename)
	{
		for (String suffix:suffixes) {
			if (filename.endsWith(suffix)) filename = removeSuffix(filename, suffix);
		}
		return filename;
	}
	
	protected static String removeSuffix(String text, String suffix)
	{
		return text.substring(0, text.length()-suffix.length());
	}
	
	public static String toValidFileName(String name)
	{
		if (name == null) return null;
		return name.replaceAll("[^a-zA-Z0-9.-]", "_");
	}

}
