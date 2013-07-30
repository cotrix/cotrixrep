/**
 * 
 */
package org.cotrix.web.importwizard.server.upload;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FileNameCleaner {
	
	protected static final String[] suffixes = {".txt", ".csv"};
	
	public static String clean(String filename)
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

}
