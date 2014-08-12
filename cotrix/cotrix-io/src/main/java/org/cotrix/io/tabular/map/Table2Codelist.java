package org.cotrix.io.tabular.map;

import static org.cotrix.common.Report.*;
import static org.cotrix.common.Report.Item.Type.*;
import static org.cotrix.domain.dsl.Data.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.io.utils.SharedDefinitionPool;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

/**
 * 
 * @author Fabio Simeoni
 *
 */
public class Table2Codelist {

	private final List<Code> codes = new ArrayList<Code>();
	private final List<Column2Attribute> attributeMappers = new ArrayList<Column2Attribute>();
	private final Table2CodelistDirectives directives;
	
	/**
	 * Creates an instance with a given directives.
	 * @param directives the directives
	 */
	public Table2Codelist(Table2CodelistDirectives directives) {
		
		this.directives=directives;
		
		for (ColumnDirectives attributeMapping : directives.columns()) 
			attributeMappers.add(new Column2Attribute(attributeMapping));
		
	}
	
	/**
	 * Returns the directives use by this mapper.
	 * 
	 * @return the mapper
	 */
	public Table2CodelistDirectives mapping() {
		return directives;
	}
	
	/**
	 * Maps a table on a codelist.
	 * @param table the table
	 * @return the codelist
	 */
	public Codelist apply(Table table) {
		
		double time = System.currentTimeMillis();

		report().log("transforming table to codelist "+directives.name());
		report().log(Calendar.getInstance().getTime().toString());
		report().log("==============================");
		
		SharedDefinitionPool defs = new SharedDefinitionPool();
		
		for (Row row : table)
			map(row,defs);
		
		Codelist list = list(defs);
		
		report().log("==============================");
		report().log("transformed table to codelist '"+directives.name()+"' with "+list.codes().size()+" codes in "+(System.currentTimeMillis()-time)/1000);
		
		return list;
		
	}
	
	/**
	 * Executes the part of the directives that pertains to a single row.
	 * 
	 * @param row the row.
	 */
	void map(Row row,SharedDefinitionPool defs) {
		
		String name = row.get(directives.codeColumn());
		
		if (!valid(name))
			return;
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (Column2Attribute attributeMapper : attributeMappers) {
			Attribute parsed = attributeMapper.map(name,row,defs);
			if (parsed != null)
				attributes.add(parsed);
		}
		
		Code code = code().name(name).attributes(attributes.toArray(new Attribute[0])).build();
		
		codes.add(code);
		
	}
	
	/**
	 * Returns the mapped codelist.
	 * <p>
	 * @return the mapped codelist
	 */
	Codelist list(Iterable<AttributeDefinition> defs) {
		
		boolean hasVersion = directives.version()!=null;
		SecondClause clause= codelist().
				name(directives.name())
				.definitions(defs)
				.with(codes.toArray(new Code[0]))
				.attributes(directives.attributes().toArray(new Attribute[0]));
		
		
		return hasVersion?clause.version(directives.version()).build():clause.build();
	}
	
	//helper
	private boolean valid(String value) {
		
		if (value==null || value.isEmpty()) {
			
			String msg = "missing code in '"+directives.name()+"'";
			
			switch(directives.mode()) {
				case strict:
					report().log(msg).as(ERROR);break;
				case log:
					report().log(msg).as(WARN);break;
				default:
			}
			
			return false;
		}
		else
			return true;
	
	}
}

