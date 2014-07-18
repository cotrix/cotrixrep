package org.cotrix.io.tabular.map;

import static org.cotrix.common.Report.*;
import static org.cotrix.common.Report.Item.Type.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
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

		report().log("transforming codelist "+list.qname()+"("+list.id()+") to table");
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
			directiveMap.put(directive.template().qname(), directive);
			
			//add other columns
			columns.add(new Column(directive.columnName()));
			
		}
		
		
		//map rows
		List<Row> rows = new ArrayList<Row>();
		
		for (Code code : list.codes()) {
			
			Map<QName,String> values = new HashMap<QName, String>();
			
			Map<QName,Attribute> attributeMatches = new HashMap<QName,Attribute>();
			Map<QName,Codelink> linkMatches = new HashMap<QName,Codelink>();
			
			//map code
			values.put(codeColumnName,code.qname().getLocalPart());
			
			//collect matches
			for (Attribute a : code.attributes())
				if (directiveMap.containsKey(a.qname()) && matches(directiveMap.get(a.qname()).template(),a))
					attributeMatches.put(directiveMap.get(a.qname()).columnName(),a);
			
			for (Codelink l : code.links())
				if (directiveMap.containsKey(l.qname()) && matches(directiveMap.get(l.qname()).template(),l))
					linkMatches.put(directiveMap.get(l.qname()).columnName(),l);
			
			//map match values in column order
			for (Column col : columns) {
				
				String error = "transformation is ambiguous: code "+code.qname()+" has multiple attributes that map onto column "+col.name();
				
				boolean matchesAttributes = attributeMatches.containsKey(col.name());
				boolean matchesLinks = linkMatches.containsKey(col.name());
				
				System.out.println("col "+col.name()+" matches attributes:"+matchesAttributes);
				System.out.println("col "+col.name()+" matches links:"+matchesLinks);
				
				if (matchesAttributes || matchesLinks) {
					
					if (values.containsKey(col.name()))
						switch (directives.mode()) {
							case STRICT:throw new IllegalStateException(error);
							case LOG: report().log(error).as(WARN);
							default:
						}
					
					values.put(col.name(),
									matchesAttributes?
											attributeMatches.get(col.name()).value():
											valueOf(linkMatches.get(col.name())));
					
				}
			}
			rows.add(new Row(values));
		}
			
		report().log("transformed codelist "+list.qname()+"("+list.id()+") to table in "+(System.currentTimeMillis()-time)/1000);
		
		return new DefaultTable(columns, rows);
	}
	
	private String valueOf(Codelink l) {
		
		List<Object> linkval = l.value();
		return linkval.isEmpty()? null:
							 	linkval.size()==1? linkval.get(0).toString() :
							 					   linkval.toString();
	}
		
	private boolean matches(Attribute template,Attribute attribute) {
		
		if (!template.qname().equals(attribute.qname()))
			return false;
		
		if (template.type()!=null && !template.type().equals(attribute.type()))
			return false;
		
		if (template.language()!=null && !template.language().equals(attribute.language()))
			return false;
		
		return true;
	}
	
	private boolean matches(Attribute template,Codelink link) {
		
		if (!template.qname().equals(link.qname()))
			return false;
		
		return true;
	}
}
