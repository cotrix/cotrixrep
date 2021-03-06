package org.cotrix.io.utils;

import static org.cotrix.domain.dsl.Data.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.dsl.grammar.AttributeDefinitionGrammar;

public class SharedDefinitionPool implements Iterable<AttributeDefinition> {

	Map<DefinitionTemplate, AttributeDefinition> defs = new HashMap<>();

	
	public AttributeDefinition get(String name, String type, String language) {
		
		return get(q(name),q(type),language);

	}

	public AttributeDefinition get(QName name, QName type) {
		
		return get(name,type,null);

	}
	
	public AttributeDefinition get(QName name, QName type, String language) {
		
		DefinitionTemplate key = key(name,type,language);
		
		AttributeDefinition def = defs.get(key);
		
		if (def==null) { 
			
			AttributeDefinitionGrammar.SecondClause clause = attrdef().name(name);
			
			if (type!=null)
				clause.is(type);
			
			if (language!=null)
				clause.in(language);
			
			def = clause.build();
			
			defs.put(key,def);
		}
		
		return def;

	}
	
	public AttributeDefinition get(String name, String type) {
		
		return get(name,type,null);

	}
	
	@Override
	public Iterator<AttributeDefinition> iterator() {
		return defs.values().iterator();
	}
	
	//helpers

	private DefinitionTemplate key(QName name, QName type, String language) {
		return new DefinitionTemplate(name, type, language);
	}

	private static class DefinitionTemplate {
		private final QName name;
		private final QName type;
		private final String language;

		DefinitionTemplate(QName name, QName type, String language) {
			this.name = name;
			this.type = type;
			this.language = language;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((language == null) ? 0 : language.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DefinitionTemplate other = (DefinitionTemplate) obj;
			if (language == null) {
				if (other.language != null)
					return false;
			} else if (!language.equals(other.language))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}

	}
}
