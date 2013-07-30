/**
 * 
 */
package org.cotrix.web.importwizard.server.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.cotrix.io.tabular.csv.CsvParseDirectives;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CsvPreviewData;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvPreviewHelper {
	
	public static CsvParseDirectives getDirectives(CsvParserConfiguration configuration)
	{
		CsvParseDirectives directives = new CsvParseDirectives();
		directives.options().hasHeader(configuration.isHasHeader());
		directives.options().setDelimiter(configuration.getFieldSeparator());
		directives.options().setQuote(configuration.getQuote());
		Charset charset = Charset.forName(configuration.getCharset());
		directives.options().setEncoding(charset);
		return directives;
	}
	
	public static CsvPreviewData convert(Table table, int rowLimit)
	{
		CsvPreviewData preview = new CsvPreviewData();
		preview.setHeader(getHeader(table));
		preview.setData(getData(table, rowLimit));
		return preview;
	}
	
	protected static List<String> getHeader(Table table)
	{
		List<Column> columns = table.columns();
		List<String> header = new ArrayList<String>(columns.size());
		for (Column column:columns) header.add(column.name().getLocalPart());
		return header;
	}
	
	protected static List<List<String>> getData(Table table, int rowLimit)
	{
		List<List<String>> data = new ArrayList<List<String>>();
		int rowCount = 0;
		for (Row row:table) {
			data.add(getRow(row, table.columns()));
			if (rowLimit>=0 && rowCount++ > rowLimit) break;
		}
		return data;
	}
	
	protected static List<String> getRow(Row row, List<Column> columns)
	{
		List<String> cells = new ArrayList<String>(columns.size());
		for (Column column:columns) cells.add(row.get(column));
		return cells;
	}

}
