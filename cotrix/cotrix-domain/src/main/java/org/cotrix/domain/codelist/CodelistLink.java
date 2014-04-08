package org.cotrix.domain.codelist;

import org.cotrix.domain.links.OccurrenceRange;
import org.cotrix.domain.links.ValueFunction;
import org.cotrix.domain.links.ValueType;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


public interface CodelistLink extends Identified, Attributed, Named {

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
	ValueType valueType();
	
	
	/**
	 * Returns the function that yields values of link instances.
	 * @return the function
	 */
	ValueFunction function();
	
	/**
	 * Returns the occurrence range for link instances.
	 * @return the range
	 */
	OccurrenceRange range();
		
	
	static interface State extends Identified.State, Attributed.State, Named.State, EntityProvider<Private> {

		Codelist.State target();
		
		ValueType valueType();
		
		void valueType(ValueType type);
		
		ValueFunction function();
		
		void function(ValueFunction function);

		void target(Codelist.State state);
		
		OccurrenceRange range();
		
		void range(OccurrenceRange type);
	}

	/**
	 * A {@link Named.Abstract} implementation of {@link CodelistLink}.
	 * 
	 */
	public class Private extends Named.Abstract<Private,State> implements CodelistLink {

		public Private(CodelistLink.State state) {
			super(state);
		}

		@Override
		public Codelist target() {
			return state().target() == null ? null : new Codelist.Private(state().target());
		}
		
		@Override
		public ValueType valueType() {
			return state().valueType();
		}
		
		
		@Override
		public ValueFunction function() {
			return state().function();
		}
		
		@Override
		public OccurrenceRange range() {
			return state().range();
		}

		@Override
		public void update(CodelistLink.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			ValueType newtype = changeset.state().valueType();
			
			if (newtype!=null)
				state().valueType(newtype);
			
			
			ValueFunction newfunction = changeset.state().function();
			
			if (newfunction!=null)
				state().function(newfunction);
			
			OccurrenceRange newrange = changeset.state().range();
			
			if (newrange!=null)
				state().range(newrange);
			
			//ignore target, DSL should have prevented this statically anyway
		}

	}
}
