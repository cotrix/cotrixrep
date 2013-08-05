/**
 * 
 */
package org.cotrix.web.importwizard.server.upload;

import org.apache.commons.fileupload.FileItem;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration.NewLine;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfigurationGuesser {
	
	public CsvParserConfiguration guessConfiguration(FileItem csvFile)
	{
		//TODO implement
		CsvParserConfiguration configuration = new CsvParserConfiguration();
		configuration.setCharset("ISO-8859-1");
		configuration.setComment('#');
		configuration.setFieldSeparator('\t');
		configuration.setHasHeader(true);
		configuration.setLineSeparator(NewLine.LF);
		configuration.setQuote('"');
		return configuration;
	}

}
