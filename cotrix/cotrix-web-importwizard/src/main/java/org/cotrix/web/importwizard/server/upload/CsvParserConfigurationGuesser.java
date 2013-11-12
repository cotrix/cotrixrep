/**
 * 
 */
package org.cotrix.web.importwizard.server.upload;

import org.apache.commons.fileupload.FileItem;
import org.cotrix.web.share.shared.CsvConfiguration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfigurationGuesser {
	
	public CsvConfiguration guessConfiguration(FileItem csvFile)
	{
		//TODO implement
		CsvConfiguration configuration = new CsvConfiguration();
		configuration.setCharset("ISO-8859-1");
		configuration.setComment('#');
		configuration.setFieldSeparator('\t');
		configuration.setHasHeader(true);
		configuration.setQuote('"');
		return configuration;
	}

}
