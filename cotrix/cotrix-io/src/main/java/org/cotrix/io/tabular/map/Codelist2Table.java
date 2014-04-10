package org.cotrix.io.tabular.map;

import static org.cotrix.common.Report.*;
import static org.cotrix.common.Report.Item.Type.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.io.impl.MapTask;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.DefaultTable;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

public class Codelist2Table implements MapTask<Codelist, Table, Codelist2TableDirectives> {

	public static final String DEFAULT_CODE_COLUMN_NAME = "code";

	@Override
	public Class<Codelist2TableDirectives> directedBy() {
		return Codelist2TableDirectives.class;
	}

	@Override
	public Table map(Codelist list, Codelist2TableDirectives directives) throws Exception {
		
		double time = System.currentTimeMillis();

		report().log("mapping codelist "+list.name()+"("+list.id()+") to table");
		report().log(Calendar.getInstance().getTime().toString());

		//generate in memory for now
		List<Column> columns = new ArrayList<Column>();
		
		QName codeColumnName = directives.codeColumnName()==null?
								new QName(DEFAULT_CODE_COLUMN_NAME):
								directives.codeColumnName();
		
		//add code column
		columns.add(new Column(codeColumnName));
		
		Map<QName,AttributeDirectives> directiveMap = new HashMap<QName, AttributeDirectives>();
		
		for (AttributeDirectives directive : directives.attributes()) {
			
			//index templates
			directiveMap.put(directive.template().name(), directive);
			
			//add other columns
			columns.add(new Column(directive.columnName()));
		}
		
		
		//map rows
		List<Row> rows = new ArrayList<Row>();
		
		for (Code code : list.codes()) {
			
			Map<QName,String> values = new HashMap<QName, String>();
			Map<QName,Attribute> matches = new HashMap<QName,Attribute>();
			
			//map code
			values.put(codeColumnName,code.name().getLocalPart());
			
			//collect matches
			for (Attribute a : code.attributes())
				if (directiveMap.containsKey(a.name()) && matches(directiveMap.get(a.name()).template(),a))
					matches.put(directiveMap.get(a.name()).columnName(),a);
			
			//map match values in column order
			for (Column col : columns) {
				String error = "mapping is ambiguous: code "+code.name()+" has multiple attributes that map onto column "+col.name();
				if (matches.containsKey(col.name())) {
					if (values.containsKey(col.name()))
						switch (directives.mode()) {
							case STRICT:throw new IllegalStateException(error);
							case LOG: report().log(error).as(WARN);
							default:
						}
					values.put(col.name(),matches.get(col.name()).value());
				}
			}
			rows.add(new Row(values));
		}
			
		report().log("mapped codelist "+list.name()+"("+list.id()+") to table in "+(System.currentTimeMillis()-time)/1000);
		
		return new DefaultTable(columns, rows);
	}
	
	

	
	private boolean matches(Attribute template,Attribute attribute) {
		
		if (!template.name().equals(attribute.name()))
			return false;
		
		if (template.type()!=null && !template.type().equals(attribute.type()))
			return false;
		
		if (template.language()!=null && !template.language().equals(attribute.language()))
			return false;
		
		return true;
	}
}
