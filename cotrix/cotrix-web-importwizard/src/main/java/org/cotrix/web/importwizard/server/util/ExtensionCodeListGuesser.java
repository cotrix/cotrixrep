/**
 * 
 */
package org.cotrix.web.importwizard.server.util;

import java.io.InputStream;

import org.cotrix.web.importwizard.shared.CodeListType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ExtensionCodeListGuesser implements CodeListTypeGuesser {
	
	protected static final String XML = "xml";
	protected static final String CSV = "csv";
	
	@Override
	public CodeListType guess(String fileName, InputStream is) {
		if (fileName == null) return null;
		String extension = getExtension(fileName);
		if (XML.equalsIgnoreCase(extension)) return CodeListType.SDMX;
		if (CSV.equalsIgnoreCase(extension)) return CodeListType.CSV;
		return null;
	}
	
	protected String getExtension(String fileName)
	{
		if (fileName == null) return null;
		if (fileName.isEmpty()) return fileName;
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex<0) return fileName;
		return fileName.substring(dotIndex);
	}

}
