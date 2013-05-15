package org.cotrix.io.tabular.csv;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.io.ingest.Report.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.io.tabular.ColumnDirectives;
import org.cotrix.io.tabular.Column2Attribute;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

/**
 * Executes a {@link CodelistMapping} to map {@link Table}s onto {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class Csv2Codelist {

	private final List<Code> codes = new ArrayList<Code>();
	private final List<Column2Attribute> attributeMappers = new ArrayList<Column2Attribute>();
	private final CsvImportDirectives directives;
	
	/**
	 * Creates an instance with a given directives.
	 * @param directives the directives
	 */
	public Csv2Codelist(CsvImportDirectives directives) {
		
		this.directives=directives;
		
		for (ColumnDirectives attributeMapping : directives.columns()) 
			attributeMappers.add(new Column2Attribute(attributeMapping));
	}
	
	/**
	 * Returns the directives use by this mapper.
	 * 
	 * @return the mapper
	 */
	public CsvImportDirectives mapping() {
		return directives;
	}
	
	/**
	 * Maps a table on a codelist.
	 * @param table the table
	 * @return the codelist
	 */
	public Codelist apply(Table table) {
		
		report().log("importing codelist '"+directives.name()+"'");
		report().log("==============================");
		
		for (Row row : table)
			map(row);
		
		Codelist list = list();
		
		report().log("==============================");
		report().log("terminated import of codelist '"+directives.name()+"' with "+list.codes().size()+" codes.");
		
		return list;
		
	}
	
	/**
	 * Executes the part of the directives that pertains to a single row.
	 * <p>
	 * Exposed for collaboration with a {@link CodebagMapper}.
	 * 
	 * @param row the row.
	 */
	void map(Row row) {
		
		String name = row.get(directives.codeColumn());
		
		if (!valid(name))
			return;
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (Column2Attribute attributeMapper : attributeMappers) {
			Attribute parsed = attributeMapper.map(name,row);
			if (parsed != null)
				attributes.add(parsed);
		}
		
		Code code = code().name(name).attributes(attributes.toArray(new Attribute[0])).build();
		
		codes.add(code);
		
	}
	
	/**
	 * Returns the mapped codelist.
	 * <p>
	 * Exposed for collaboration with a {@link CodebagMapper}.
	 * @return the mapped codelist
	 */
	Codelist list() {
		
		return codelist().
				name(directives.name())
				.with(codes.toArray(new Code[0]))
				.attributes(directives.attributes().toArray(new Attribute[0])).build();
	}
	
	//helper
	private boolean valid(String value) {
		
		if (value==null || value.isEmpty()) {
			
			String msg = "missing code in '"+directives.name()+"'";
			
			switch(directives.mode()) {
				case STRICT:
					report().logError(msg);break;
				case LOG:
					report().logWarning(msg);break;
			}
			
			return false;
		}
		else
			return true;
	
	}
}

