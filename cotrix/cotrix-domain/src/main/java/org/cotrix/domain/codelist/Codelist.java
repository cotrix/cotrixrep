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
import org.cotrix.domain.version.Version;



public interface Codelist extends Identified,Attributed,Named {

	//public read-only interface
	
	Container<? extends Code> codes();
	
	Container<? extends LinkDefinition> links();

	Container<? extends AttributeDefinition> definitions();
	
	String previousVersion();
	
	String version();
	
	//private state interface
	
	interface Bean extends Attributed.Bean, BeanOf<Private> {
	
		BeanContainer<Code.Bean> codes();
		
		BeanContainer<LinkDefinition.Bean> links();
		
		BeanContainer<AttributeDefinition.Bean> definitions();
		
		Version version();
		
		void version(Version version);
		
	}
	

	
	
	//private logic
	
	final class Private extends Attributed.Private<Private,Bean> implements Codelist {
		
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
		public String version() {
			return bean().version() == null ? null : bean().version().value();
		}
		
		@Override
		public String previousVersion() {
			
			return valueOf(PREVIOUS_VERSION);
			
		}


		@Override
		public String toString() {
			return "Codelist [id="+id()+", name=" + qname() + ", codes=" + codes() + ", attributes=" + attributes() + ", links=" + links() + ", version="
					+ version() + (status()==null?"":" ("+status()+") ")+"]";
		}

		public Codelist bump(String version) {

			Version newVersion = bean().version().bumpTo(version);

			Codelist.Bean bean = new MCodelist(bean());
			
			bean.version(newVersion);

			return bean.entity();

		}
		
		@Override
		public void update(Codelist.Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			//detect illegal version changes
			if (changeset.version() != null && !changeset.version().equals(this.version()))
				throw new IllegalArgumentException("cannot change the version (" + version() + ") of entity " + id()
						+ ". Versioning is performed by copy");
			
			definitions().update(changeset.definitions());
			
			links().update(changeset.links());
			
			codes().update(changeset.codes());
			
		
		}
		
	}
}