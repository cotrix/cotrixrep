/**
 * 
 */
package org.cotrix.web.ingest.server.upload;

import org.cotrix.web.ingest.shared.UIAssetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListTypeGuesser {
	
	protected static final String XML_EXTENSION = "xml";
	protected static final String CSV_EXTENSION = "csv";
	
	protected static final String[] CSV_MIME_TYPES = {"text/csv","text/plain","application/vnd.ms-excel","text/comma-separated-predefinedUsers","application/csv","application/excel","application/vnd.msexcel","text/anytex"};
	protected static final String[] XML_MIME_TYPES = {"application/xml","text/xml"};
	
	protected Logger logger = LoggerFactory.getLogger(CodeListTypeGuesser.class);
	
	public UIAssetType guess(String fileName, String contentType)
	{
		logger.trace("guessing codelist type with filename {} and content type {}", fileName, contentType);
		UIAssetType type = guessByContentType(contentType);
		logger.trace("guessing by content type: {}",type);
		if (type!=null) return type;
		type = guessByExtension(fileName);
		logger.trace("guessing by file extension: {}",type);
		return type;
	}
	

	
	protected UIAssetType guessByContentType(String contentType)
	{
		for (String csvMimeType:CSV_MIME_TYPES) if (csvMimeType.equalsIgnoreCase(contentType)) return UIAssetType.CSV;
		for (String xmlMimeType:XML_MIME_TYPES) if (xmlMimeType.equalsIgnoreCase(xmlMimeType)) return UIAssetType.SDMX;
		return null;
	}
	

	protected UIAssetType guessByExtension(String fileName) {
		if (fileName == null) return null;
		String extension = getExtension(fileName);
		if (XML_EXTENSION.equalsIgnoreCase(extension)) return UIAssetType.SDMX;
		if (CSV_EXTENSION.equalsIgnoreCase(extension)) return UIAssetType.CSV;
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
