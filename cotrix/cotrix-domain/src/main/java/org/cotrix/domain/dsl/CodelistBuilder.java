package org.cotrix.domain.dsl;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codes.Code;
import org.cotrix.domain.codes.Codelist;
import org.cotrix.domain.versions.SimpleVersion;
import org.cotrix.domain.versions.Version;

public class CodelistBuilder {

	private final Codelist list;
	private  Version version;
	
	public CodelistBuilder(QName name) {
		this.list=new Codelist(name);
	}
	
	public CodelistBuilder(Codelist list) {
		notNull("codelist",list);
		this.list=list.copy();
	}

	public CodelistBuilder with(Code ... codes) {
		notNull("codes",codes);
		for (Code code : codes)
			list.codes().add(code);
		return this;
	}
	
	public CodelistBuilder with(Attribute ... attributes) {
		notNull("attributes",attributes);
		for (Attribute a : attributes)
			list.attributes().add(a);
		return this;
	}
	
	public CodelistBuilder version(Version version) {
		this.version=version;
		return this;
	}
	
	public CodelistBuilder version(String version) {
		this.version=new SimpleVersion(version);
		return this;
	}
	
	public Codelist build() {
		
		return version==null?
					list:
					new Codelist(list.name(),list.codes(),list.attributes(),version);
		
	}
	
}
