package org.cotrix.domain.codelist;

import static org.cotrix.domain.attributes.CommonDefinition.*;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.MCodelist;
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
	
	String previousVersion();
	
	
	
	
	//private state interface
	
	interface Bean extends Versioned.Bean, BeanOf<Private> {
	
		BeanContainer<Code.Bean> codes();
		
		BeanContainer<LinkDefinition.Bean> links();
		
		BeanContainer<AttributeDefinition.Bean> definitions();
		
	}
	

	
	
	//private logic
	
	final class Private extends Versioned.Private<Private,Bean> implements Codelist {
		
		public Private( Codelist.Bean state) {
			super(state);
		}

		
		@Override
		public Container.Private<Code.Private,Code.Bean> codes() {
			return new Container.Private<>(bean().codes());
		}
		
		@Override
		public Container.Private<LinkDefinition.Private,LinkDefinition.Bean> links() {
			return new Container.Private<>(bean().links());
		}
		
		@Override
		public Container.Private<AttributeDefinition.Private,AttributeDefinition.Bean> definitions() {
			return new Container.Private<>(bean().definitions());
		}
		
		@Override
		public String previousVersion() {
			
			return valueOf(PREVIOUS_VERSION);
			
		}

		@Override
		protected final Codelist.Private copyWith(Version version) {
			
			
			Codelist.Bean state = new MCodelist(bean());
			
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