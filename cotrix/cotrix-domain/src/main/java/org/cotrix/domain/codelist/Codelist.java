package org.cotrix.domain.codelist;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.common.Container.AttributeDefinitions;
import org.cotrix.domain.common.Container.Codes;
import org.cotrix.domain.common.Container.LinkDefinitions;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.MCodelist;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Described;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.version.Version;



public interface Codelist extends Identified,Described,Named {

	Codes codes();
	
	LinkDefinitions links();

	AttributeDefinitions definitions();
	
	String version();
	
	
	//----------------------------------------
	
	interface Bean extends Described.Bean, BeanOf<Private> {
	
		BeanContainer<Code.Bean> codes();
		
		BeanContainer<LinkDefinition.Bean> links();
		
		BeanContainer<AttributeDefinition.Bean> definitions();
		
		Version version();
		
		void version(Version version);
		
	}
	

	
	
	//private logic
	
	final class Private extends Described.Private<Private,Bean> implements Codelist {
		
		public Private( Codelist.Bean bean) {
			super(bean);
		}

		
		@Override
		public Codes codes() {
			return new Codes(bean().codes());
		}
		
		@Override
		public LinkDefinitions links() {
			return new LinkDefinitions(bean().links());
		}
		
		@Override
		public AttributeDefinitions definitions() {
			return new AttributeDefinitions(bean().definitions());
		}
		
		@Override
		public String version() {
			return bean().version() == null ? null : bean().version().value();
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
		


		@Override
		public String toString() {
			return "Codelist [id="+id()+", name=" + qname() + ", codes=" + codes() + ", attributes=" + attributes() + ", links=" + links() + ", version="
					+ version() + (status()==null?"":" ("+status()+") ")+"]";
		}
		
	}
}