/**
 * 
 */
package org.cotrix.web.ingest.server.upload;

import java.io.IOException;
import java.io.InputStream;

import org.cotrix.web.common.shared.CsvConfiguration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfigurationGuesser {
	
	public CsvConfiguration guessConfiguration(String fileName, InputStream inputStream) throws IOException
	{
		inputStream.close();
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
