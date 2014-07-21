package org.cotrix.io.tabular.map;

import static org.cotrix.io.tabular.map.MappingMode.*;
import static org.cotrix.io.tabular.map.MemberDirective.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Definition;
import org.cotrix.io.MapService.MapDirectives;
import org.virtualrepository.tabular.Table;

/**
 * Directives for mapping codelists onto tables.
 * 
 * @author Fabio Simeoni
 */
public class Codelist2TableDirectives implements MapDirectives<Table> {


	private QName codecol;
	private MappingMode mode = strict;
	
	private Map<String, MemberDirective<?>> memberDirectives = new HashMap<>();

	
	//------ API

	public Codelist2TableDirectives() {}


	public Codelist2TableDirectives codeColumn(String name) {
		return codeColumn(new QName(name));
	}
	
	public Codelist2TableDirectives codeColumn(QName name) {
		this.codecol=name;
		return this;
	}
	
	public Codelist2TableDirectives mode(MappingMode mode) {
		this.mode = mode;
		return this;
	}
	
	public Codelist2TableDirectives add(MemberDirective<?> directive) {
		 memberDirectives.put(directive.definition().id(),directive);
		 return this;
	}
	
	
	//------ SPI
	
	
	QName codeColumnName() {
		return codecol;
	}

	
	Collection<MemberDirective<?>> members() {
		return memberDirectives.values();
	}
	
	MemberDirective<?> member(Defined<?> m) {
		return memberDirectives.get(m.definition().id());
	}
	
	
	MappingMode mode() {
		return mode;
	}
	
	//----------- test API: conveniences for default mappings
	
	public Codelist2TableDirectives add(Definition definition) {
		 return add(map(definition));
	}
	
	public Codelist2TableDirectives add(Defined<?> defined) {

		 return add(defined.definition());
	}
}
