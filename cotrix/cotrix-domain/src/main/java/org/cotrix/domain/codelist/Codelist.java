package org.cotrix.domain.codelist;

import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.memory.CodelistMS;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.Version;



/**
 * An {@link Identified}, {@link Attribute}, {@link Named}, and {@link Versioned} set of {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codelist extends Identified,Attributed,Named,Versioned {

	//public read-only interface
	
	/**
	 * Returns the codes of this list.
	 * @return the codes
	 */
	NamedContainer<? extends Code> codes();
	
	/**
	 * Returns the links of this list.
	 * @return the links.
	 */
	NamedContainer<? extends LinkDefinition> links();

	
	/**
	 * Returns the attribute types of this list.
	 * @return the attribute types.
	 */
	NamedContainer<? extends AttributeDefinition> definitions();
	
	//private state interface
	
	interface State extends Identified.State, Attributed.State, Named.State, Versioned.State, EntityProvider<Private> {
	
		NamedStateContainer<Code.State> codes();
		
		NamedStateContainer<LinkDefinition.State> links();
		
		NamedStateContainer<AttributeDefinition.State> definitions();
		
	}
	
	//private logic
	
	final class Private extends Versioned.Abstract<Private,State> implements Codelist {
		
		public Private( Codelist.State state) {
			super(state);
		}

		
		@Override
		public NamedContainer.Private<Code.Private,Code.State> codes() {
			return namedContainer(state().codes());
		}
		
		@Override
		public NamedContainer.Private<LinkDefinition.Private,LinkDefinition.State> links() {
			return namedContainer(state().links());
		}
		
		@Override
		public NamedContainer.Private<AttributeDefinition.Private,AttributeDefinition.State> definitions() {
			return namedContainer(state().definitions());
		}

		@Override
		protected final Private copyWith(Version version) {
			
			
			Codelist.State state = new CodelistMS(state());
			
			state.version(version);

			return state.entity();
		}

		@Override
		public String toString() {
			return "Codelist [id="+id()+", name=" + qname() + ", codes=" + codes() + ", attributes=" + attributes() + ", links=" + links() + ", version="
					+ version() + (status()==null?"":" ("+status()+") ")+"]";
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			definitions().update(changeset.definitions());
			
			links().update(changeset.links());
			
//			if (attributes().contains(PREVIOUS_VERSION))
//				markNewCodes(changeset.codes());
				
			codes().update(changeset.codes());
			
		
		}
		
//		private void markNewCodes(NamedContainer.Private<Code.Private,Code.State>  codes) {
//			
//			for (Code.Private code : codes)
//				
//				if (code.status()==null) {
//					
//					NamedStateContainer<Attribute.State> attributes = code.state().attributes();
//					
//					attributes.add(stateof(attribute().with(NEW).value("true")));
//				
//				}
//		}
	}
}