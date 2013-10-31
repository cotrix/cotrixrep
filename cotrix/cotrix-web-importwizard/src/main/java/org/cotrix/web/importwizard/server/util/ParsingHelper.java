/**
 * 
 */
package org.cotrix.web.importwizard.server.util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.parse.ParseService;
import org.cotrix.io.sdmx.SdmxParseDirectives;
import org.cotrix.io.tabular.csv.CsvParseDirectives;
import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.share.shared.CsvParserConfiguration;
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
	
	public static final int ROW_LIMIT = 5;
	
	@Inject
	ParseService service;
	
	public PreviewData getCsvPreviewData(CsvParserConfiguration parserConfiguration, InputStream inputStream)
	{
		logger.trace("creating preview");

		Table table = parse(parserConfiguration, inputStream);

		logger.trace("converting");
		PreviewData previewData = convert(table, !parserConfiguration.isHasHeader(), ROW_LIMIT);
		logger.trace("ready");
		
		return previewData;
	}
	
	public Table parse(CsvParserConfiguration parserConfiguration, InputStream inputStream)
	{

		CsvParseDirectives directives = getDirectives(parserConfiguration);

		logger.trace("parsing");
		Table table = service.parse(inputStream, directives);
		
		return table;
	}
	
	public CodelistBean parse(InputStream inputStream) {
		SdmxParseDirectives directives = SdmxParseDirectives.DEFAULT;
		
		logger.trace("parsing");
		CodelistBean codelistBean = service.parse(inputStream, directives);
		return codelistBean;
	}
	
	public CsvParseDirectives getDirectives(CsvParserConfiguration configuration)
	{
		CsvParseDirectives directives = new CsvParseDirectives();
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
		for (Row row:table) {
			data.add(getRow(row, table.columns()));
			if (rowLimit>=0 && rowCount++ > rowLimit) break;
		}
		return data;
	}
	
	protected List<String> getRow(Row row, List<Column> columns)
	{
		List<String> cells = new ArrayList<String>(columns.size());
		for (Column column:columns) cells.add(row.get(column));
		return cells;
	}

}
