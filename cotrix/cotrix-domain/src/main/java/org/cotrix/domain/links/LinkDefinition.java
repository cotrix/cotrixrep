package org.cotrix.domain.links;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Definition;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.values.ValueFunction;


public interface LinkDefinition extends Attributed, Definition {

	Codelist target();
	
	LinkValueType valueType();
	
	ValueFunction function();
	
	Range range();
		
	
	
	
	
	static interface Bean extends Identified.Bean, Attributed.Bean, Named.Bean, BeanOf<Private> {

		Codelist.Bean target();
		
		LinkValueType valueType();
		
		void valueType(LinkValueType type);
		
		ValueFunction function();
		
		void function(ValueFunction function);

		void target(Codelist.Bean state);
		
		Range range();
		
		void range(Range type);
	}

	
	
	
	
	public class Private extends Attributed.Private<Private,Bean> implements LinkDefinition {

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
		public Range range() {
			return bean().range();
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
			
			Range newrange = changeset.bean().range();
			
			if (newrange!=null)
				bean().range(newrange);
			
			//ignore target, DSL should have prevented this statically anyway
		}

	}
}
