package org.cotrix.io.tabular.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.io.MapService.MapDirectives;
import org.virtualrepository.tabular.Table;

/**
 * Directives for mapping codelists into tables.
 * <p>
 * The mapping is defined by:
 * 
 * <ul>
 * <li>(optional) the <b>name</b> of the target codelist. If unspecified, the codelist is named after the code column.
 * <li>(optional) the <b>attributes</b> of the target codelist.
 * <li>(optional) <b>column directives</b>, the directives for mapping other columns.
 * <li> (optional) the <b>mapping mode</b> for missing values in the code column, {@link MappingMode#STRICT} by default (#cf {@link MappingMode}).
 * </ul>
 */
public class Codelist2TableDirectives implements MapDirectives<Table> {


	private QName codeColumnName;
	
	private Map<String, MemberDirective<?>> memberDirectives = new HashMap<>();
	
	private MappingMode mode = MappingMode.STRICT;
	
	public Codelist2TableDirectives() {}


	public QName codeColumnName() {
		return codeColumnName;
	}

	public Codelist2TableDirectives codeColumnName(String name) {
		return codeColumnName(new QName(name));
	}
	
	public Codelist2TableDirectives codeColumnName(QName name) {
		this.codeColumnName=name;
		return this;
	}

	public Codelist2TableDirectives add(MemberDirective<?> directive) {
		 memberDirectives.put(directive.definition().id(),directive);
		 return this;
	}
	
	public <T extends Named & Identified> Codelist2TableDirectives add(T definition) {
		 return add(new MemberDirective<T>(definition));
	}
	
	public Collection<MemberDirective<?>> members() {
		return memberDirectives.values();
	}
	
	public <T extends Identified> MemberDirective<?> member(Defined<T> m) {
		return memberDirectives.get(m.definition().id());
	}
	
	public Codelist2TableDirectives mode(MappingMode mode) {
		this.mode = mode;
		return this;
	}
	
	public MappingMode mode() {
		return mode;
	}
}
