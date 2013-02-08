package org.cotrix.domain.dsl;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codes.Codebag;
import org.cotrix.domain.codes.Codelist;
import org.cotrix.domain.versions.SimpleVersion;
import org.cotrix.domain.versions.Version;

public class CodebagBuilder {

	private final Codebag bag;
	private  Version version;
	
	public CodebagBuilder(QName name) {
		this.bag=new Codebag(name);
	}
	
	public CodebagBuilder(Codebag list) {
		notNull("codebag",list);
		this.bag=list.copy();
	}

	public CodebagBuilder with(Codelist ... lists) {
		notNull("code lists",lists);
		for (Codelist code : lists)
			bag.lists().add(code);
		return this;
	}
	
	public CodebagBuilder with(Attribute ... attributes) {
		notNull("attributes",attributes);
		for (Attribute a : attributes)
			bag.attributes().add(a);
		return this;
	}
	
	public CodebagBuilder version(Version version) {
		this.version=version;
		return this;
	}
	
	public CodebagBuilder version(String version) {
		this.version=new SimpleVersion(version);
		return this;
	}
	
	public Codebag build() {
		
		return version==null?
					bag:
					new Codebag(bag.name(),bag.lists(),bag.attributes(),version);
		
	}
	
}
