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

		//------- add columns and prepare lookup structure (awkward to extrapolate)
		
		List<Column> columns = new ArrayList<Column>();
		
		QName codeColumnName = directives.codeColumnName()==null?
									new QName(DEFAULT_CODE_COLUMN_NAME):
										directives.codeColumnName();		
		columns.add(new Column(codeColumnName));
		
		Map<QName,AttributeDirective> attributeDirectiveMap = new HashMap<QName, AttributeDirective>();
		
		for (AttributeDirective directive : directives.attributes()) {
		
			attributeDirectiveMap.put(directive.template().qname(), directive);
			columns.add(new Column(directive.columnName()));
			
		}
		
		Map<QName,LinkDirective> linkDirectivesMap = new HashMap<QName, LinkDirective>();
		
		for (LinkDirective directive : directives.links()) {
		
			linkDirectivesMap.put(directive.template().qname(), directive);
			columns.add(new Column(directive.columnName()));
			
		}
		
		
		//-------- map rows
		
		List<Row> rows = new ArrayList<Row>();
		
		for (Code code : list.codes()) {
			
			Map<QName,String> values = new HashMap<QName, String>();
			
			Map<QName,Attribute> attributeMatches = new HashMap<QName,Attribute>();
			Map<QName,Codelink> linkMatches = new HashMap<QName,Codelink>();
			
			//map code
			values.put(codeColumnName,code.qname().getLocalPart());
			
			//collect matches
			for (Attribute a : code.attributes())
				if (matches(attributeDirectiveMap.get(a.qname()),a))
					attributeMatches.put(attributeDirectiveMap.get(a.qname()).columnName(),a);
			
			for (Codelink l : code.links())
				if (matches(linkDirectivesMap.get(l.qname()),l))
					linkMatches.put(attributeDirectiveMap.get(l.qname()).columnName(),l);
			
			//map match values in column order
			for (Column col : columns) {
				
				String error = "transformation is ambiguous: code "+code.qname()+" has multiple attributes that map onto column "+col.name();
				
				boolean matchesAttributes = attributeMatches.containsKey(col.name());
				boolean matchesLinks = linkMatches.containsKey(col.name());
				
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
		
	private boolean matches(AttributeDirective directive,Attribute attribute) {
		
		if (directive==null)
			return false;
		
		Attribute template = directive.template();
		
		if (!template.qname().equals(attribute.qname()))
			return false;
		
		if (template.type()!=null && !template.type().equals(attribute.type()))
			return false;
		
		if (template.language()!=null && !template.language().equals(attribute.language()))
			return false;
		
		return true;
	}
	
	private boolean matches(LinkDirective directive,Codelink link) {
		
		return directive==null?false:directive.template().matches(link.type());
	}
}
