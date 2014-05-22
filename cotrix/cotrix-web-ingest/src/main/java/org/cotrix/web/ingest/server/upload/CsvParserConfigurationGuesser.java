/**
 * 
 */
package org.cotrix.web.ingest.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Singleton;

import org.cotrix.web.common.shared.CsvConfiguration;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CsvParserConfigurationGuesser {
	
	private Logger logger = LoggerFactory.getLogger(CsvParserConfigurationGuesser.class);
	
	public CsvConfiguration guessConfiguration(String fileName, File file) throws IOException
	{
		CsvConfiguration configuration = new CsvConfiguration();
		
		String guessedEncoding = guessEncoding(file);
		logger.trace("guessed encoding: "+guessedEncoding);
		String encoding = guessedEncoding!=null?guessedEncoding:"ISO-8859-1";
		configuration.setCharset(encoding);
		
		configuration.setComment('#');
		configuration.setFieldSeparator('\t');
		configuration.setHasHeader(true);
		configuration.setQuote('"');
		return configuration;
	}
	
	private String guessEncoding(File file) throws IOException
	{
		FileInputStream fis = new FileInputStream(file);
		UniversalDetector detector = new UniversalDetector(null);
		byte[] buf = new byte[4096];

		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}

		detector.dataEnd();
		String encoding = detector.getDetectedCharset();
		detector.reset();
		fis.close();

		return encoding;
	}

}
