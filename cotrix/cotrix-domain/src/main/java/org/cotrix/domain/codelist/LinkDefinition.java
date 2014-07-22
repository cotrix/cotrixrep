package org.cotrix.domain.codelist;

import org.cotrix.domain.common.Range;
import org.cotrix.domain.links.LinkValueType;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Definition;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.values.ValueFunction;


public interface LinkDefinition extends Attributed, Definition {

	/**
	 * Returns the target codelist.
	 * 
	 * @return the target codelist
	 */
	Codelist target();
	
	/**
	 * Returns the type of values of link instances.
	 * @return the type
	 */
	LinkValueType valueType();
	
	
	/**
	 * Returns the function that yields values of link instances.
	 * @return the function
	 */
	ValueFunction function();
	
	/**
	 * Returns the occurrence range for link instances.
	 * @return the range
	 */
	Range range();
		
	
	static interface State extends Identified.State, Attributed.State, Named.State, EntityProvider<Private> {

		Codelist.State target();
		
		LinkValueType valueType();
		
		void valueType(LinkValueType type);
		
		ValueFunction function();
		
		void function(ValueFunction function);

		void target(Codelist.State state);
		
		Range range();
		
		void range(Range type);
	}

	/**
	 * A {@link Named.Abstract} implementation of {@link LinkDefinition}.
	 * 
	 */
	public class Private extends Named.Abstract<Private,State> implements LinkDefinition {

		public Private(LinkDefinition.State state) {
			super(state);
		}

		@Override
		public Codelist target() {
			return state().target() == null ? null : new Codelist.Private(state().target());
		}
		
		@Override
		public LinkValueType valueType() {
			return state().valueType();
		}
		
		
		@Override
		public ValueFunction function() {
			return state().function();
		}
		
		@Override
		public Range range() {
			return state().range();
		}

		@Override
		public void update(LinkDefinition.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			LinkValueType newtype = changeset.state().valueType();
			
			if (newtype!=null)
				state().valueType(newtype);
			
			
			ValueFunction newfunction = changeset.state().function();
			
			if (newfunction!=null)
				state().function(newfunction);
			
			Range newrange = changeset.state().range();
			
			if (newrange!=null)
				state().range(newrange);
			
			//ignore target, DSL should have prevented this statically anyway
		}

	}
}