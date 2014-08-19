package org.cotrix.domain.links;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Definition;
import org.cotrix.domain.trait.Described;
import org.cotrix.domain.values.ValueFunction;


public interface LinkDefinition extends Described,Definition {

	Codelist target();
	
	LinkValueType valueType();
	
	ValueFunction function();
		
	
	static interface Bean extends Definition.Bean, BeanOf<Private> {

		Codelist.Bean target();
		
		LinkValueType valueType();
		
		void valueType(LinkValueType type);
		
		ValueFunction function();
		
		void function(ValueFunction function);

		void target(Codelist.Bean state);
	}

	
	public class Private extends Definition.Private<Private,Bean> implements LinkDefinition {

		public Private(LinkDefinition.Bean state) {
			super(state);
		}

		@Override
		public Codelist target() {
			return bean().target() == null ? null : new Codelist.Private(bean().target());
		}
		
		@Override
		public LinkValueType valueType() {
			return bean().valueType();
		}
		
		
		@Override
		public ValueFunction function() {
			return bean().function();
		}
		
		@Override
		public void update(LinkDefinition.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			LinkValueType newtype = changeset.bean().valueType();
			
			if (newtype!=null)
				bean().valueType(newtype);
			
			
			ValueFunction newfunction = changeset.bean().function();
			
			if (newfunction!=null)
				bean().function(newfunction);
			
			//ignore target, DSL should have prevented this statically anyway
		}

	}
}
