package org.cotrix.domain.dsl;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codes.Code;

public class CodeBuilder {

	private final Code code;
	
	public CodeBuilder(QName name) {
		this.code=new Code(name);
	}
	
	public CodeBuilder(Code code) {
		notNull("code",code);
		this.code=code.copy();
	}

	public CodeBuilder with(Attribute ... attributes) {
		notNull(attributes);
		for (Attribute a : attributes)
			code.attributes().add(a);
		return this;
	}
	
	public Code build() {
		return code;
	}
	
}
