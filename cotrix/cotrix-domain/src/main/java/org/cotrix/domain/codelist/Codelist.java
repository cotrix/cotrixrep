package org.cotrix.domain.codelist;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.CodelistMS;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.Version;



public interface Codelist extends Identified,Attributed,Named,Versioned {

	//public read-only interface
	
	Container<? extends Code> codes();
	
	Container<? extends LinkDefinition> links();

	Container<? extends AttributeDefinition> definitions();
	
	
	
	
	//private state interface
	
	interface Bean extends Identified.Bean, Attributed.Bean, Named.Bean, Versioned.State, BeanOf<Private> {
	
		BeanContainer<Code.Bean> codes();
		
		BeanContainer<LinkDefinition.State> links();
		
		BeanContainer<AttributeDefinition.State> definitions();
		
	}
	

	
	
	//private logic
	
	final class Private extends Versioned.Abstract<Private,Bean> implements Codelist {
		
		public Private( Codelist.Bean state) {
			super(state);
		}

		
		@Override
		public Container.Private<Code.Private,Code.Bean> codes() {
			return new Container.Private<>(bean().codes());
		}
		
		@Override
		public Container.Private<LinkDefinition.Private,LinkDefinition.State> links() {
			return new Container.Private<>(bean().links());
		}
		
		@Override
		public Container.Private<AttributeDefinition.Private,AttributeDefinition.State> definitions() {
			return new Container.Private<>(bean().definitions());
		}

		@Override
		protected final Codelist.Private copyWith(Version version) {
			
			
			Codelist.Bean state = new CodelistMS(bean());
			
			state.version(version);

			return state.entity();
		}

		@Override
		public String toString() {
			return "Codelist [id="+id()+", name=" + qname() + ", codes=" + codes() + ", attributes=" + attributes() + ", links=" + links() + ", version="
					+ version() + (status()==null?"":" ("+status()+") ")+"]";
		}

		@Override
		public void update(Codelist.Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			definitions().update(changeset.definitions());
			
			links().update(changeset.links());
			
			codes().update(changeset.codes());
			
		
		}
		
	}
}