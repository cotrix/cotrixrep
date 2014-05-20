/**
 * 
 */
package org.cotrix.web.ingest.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.ParseService;
import org.cotrix.io.sdmx.parse.Stream2SdmxDirectives;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.ingest.shared.PreviewData;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ParsingHelper {
	
	protected Logger logger = LoggerFactory.getLogger(ParsingHelper.class);
	
	public static final int ROW_LIMIT = -1;
	
	@Inject
	ParseService service;
	
	public Table parse(CsvConfiguration parserConfiguration, InputStream inputStream) throws IOException
	{
		Csv2TableDirectives directives = getDirectives(parserConfiguration);

		logger.trace("parsing");
		Table table = service.parse(inputStream, directives);

		return table;
	}
	
	public CodelistBean parse(InputStream inputStream) {
		try {
			Stream2SdmxDirectives directives = Stream2SdmxDirectives.DEFAULT;
		
			logger.trace("parsing");
			CodelistBean codelistBean = service.parse(inputStream, directives);
			return codelistBean;
		} catch(Throwable e) {
			throw new InvalidSdmxException("Parsing failed", e);
		}
	}
	
	public Csv2TableDirectives getDirectives(CsvConfiguration configuration)
	{
		logger.trace("getDirectives configuration: {}", configuration);
		Csv2TableDirectives directives = new Csv2TableDirectives();
		directives.options().hasHeader(configuration.isHasHeader());
		directives.options().setDelimiter(configuration.getFieldSeparator());
		directives.options().setQuote(configuration.getQuote());
		Charset charset = Charset.forName(configuration.getCharset());
		directives.options().setEncoding(charset);
		return directives;
	}
	
	public PreviewData convert(Table table, boolean headersEditable, int rowLimit)
	{
		List<List<String>> rows = getRows(table, rowLimit);
		List<String> headersLabels = getHeadersLabels(table);
		PreviewData preview = new PreviewData(headersLabels, headersEditable, rows);
		return preview;
	}
	
	protected List<String> getHeadersLabels(Table table)
	{
		List<Column> columns = table.columns();
		logger.trace("columns: "+columns.size());
		List<String> header = new ArrayList<String>(columns.size());
		for (Column column:columns) header.add(column.name().getLocalPart());
		return header;
	}
	
	protected List<List<String>> getRows(Table table, int rowLimit)
	{
		List<List<String>> data = new ArrayList<List<String>>();
		int rowCount = 0;
		table.iterator();
		for (Row row:table) {
			data.add(getRow(row, table.columns()));
			if (rowLimit>=0 && rowCount++ > rowLimit) break;
		}
		logger.trace("rows: "+data.size());
		return data;
	}
	
	protected List<String> getRow(Row row, List<Column> columns)
	{
		List<String> cells = new ArrayList<String>(columns.size());
		for (Column column:columns) cells.add(row.get(column));
		return cells;
	}
	
	public class InvalidSdmxException extends RuntimeException {

		private static final long serialVersionUID = 4813637715277610637L;

		/**
		 * @param message
		 * @param cause
		 */
		public InvalidSdmxException(String message, Throwable cause) {
			super(message, cause);
		}
	}

}
