/**
 * 
 */
package org.cotrix.web.ingest.server.upload;

import java.io.InputStream;

import org.cotrix.web.ingest.shared.UIAssetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListTypeGuesser {
	
	private static final String XML_EXTENSION = "xml";
	private static final String CSV_EXTENSION = "csv";
	
	private static final String[] CSV_MIME_TYPES = {"text/csv","text/plain","application/vnd.ms-excel","text/comma-separated-predefinedUsers","application/csv","application/excel","application/vnd.msexcel","text/anytex"};
	private static final String[] XML_MIME_TYPES = {"application/xml","text/xml"};
	
	private Logger logger = LoggerFactory.getLogger(CodeListTypeGuesser.class);
	
	public UIAssetType guess(String fileName, String contentType, InputStream inputStream)
	{
		logger.trace("guessing codelist type with filename {} and content type {}", fileName, contentType);
		UIAssetType type = guessByBrowserContentType(contentType);
		logger.trace("guessed by browser content type: {}",type);
		if (type!=null) return type;
		
		type = guessByExtension(fileName);
		logger.trace("guessed by file extension: {}",type);
		return type;
	}
	
	private UIAssetType guessByBrowserContentType(String contentType)
	{
		return fromMimeTypeToAssetType(contentType);
	}
	
	private UIAssetType fromMimeTypeToAssetType(String mimeType) {
		for (String csvMimeType:CSV_MIME_TYPES) if (csvMimeType.equalsIgnoreCase(mimeType)) return UIAssetType.CSV;
		for (String xmlMimeType:XML_MIME_TYPES) if (xmlMimeType.equalsIgnoreCase(mimeType)) return UIAssetType.SDMX;
		return null;
	}
	
	private UIAssetType guessByExtension(String fileName) {
		if (fileName == null) return null;
		String extension = getExtension(fileName);
		if (XML_EXTENSION.equalsIgnoreCase(extension)) return UIAssetType.SDMX;
		if (CSV_EXTENSION.equalsIgnoreCase(extension)) return UIAssetType.CSV;
		return null;
	}
	
	private String getExtension(String fileName)
	{
		if (fileName == null) return null;
		if (fileName.isEmpty()) return fileName;
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex<0) return fileName;
		return fileName.substring(dotIndex);
	}
}
