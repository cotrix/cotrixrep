package org.cotrix.domain.codelist;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.Containers.AttributeDefinitions;
import org.cotrix.domain.common.Containers.Codes;
import org.cotrix.domain.common.Containers.LinkDefinitions;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.MCodelist;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Described;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;



public interface Codelist extends Identified,Described,Named {

	Codes codes();
	
	LinkDefinitions linkDefinitions();

	AttributeDefinitions attributeDefinitions();
	
	String version();
	
	
	//----------------------------------------
	
	interface Bean extends Described.Bean, BeanOf<Private> {
	
		Container.Bean<Code.Bean> codes();
		
		Container.Bean<LinkDefinition.Bean> linkDefinitions();
		
		Container.Bean<AttributeDefinition.Bean> attributeDefinitions();
		
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
			return new Codes(bean());
		}
		
		@Override
		public LinkDefinitions linkDefinitions() {
			return new LinkDefinitions(bean());
		}
		
		@Override
		public AttributeDefinitions attributeDefinitions() {
			return new AttributeDefinitions(bean());
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
			
			attributeDefinitions().update(changeset.attributeDefinitions());
			
			linkDefinitions().update(changeset.linkDefinitions());
			
			codes().update(changeset.codes());
			
		
		}
		


		@Override
		public String toString() {
			return "Codelist [id="+id()+", name=" + qname() + ", codes=" + codes() + ", attributes=" + attributes() + ", attrdefs=" + attributeDefinitions() + ", linkdefs=" + linkDefinitions() + ", version="
					+ version() + (status()==null?"":" ("+status()+") ")+"]";
		}
		
	}
}