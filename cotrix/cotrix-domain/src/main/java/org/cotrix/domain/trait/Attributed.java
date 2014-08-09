package org.cotrix.domain.trait;

import static java.text.DateFormat.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.BeanContainer;

/**
 * A domain object with {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Attributed {

	
	//public read-only interface
	
	Container<? extends Attribute> attributes();
	
	Date created();
	
	Date lastUpdated();
	
	String lastUpdatedBy();
	
	String originId();
	
	//private state interface
	
	interface Bean {
		
		BeanContainer<Attribute.Bean> attributes();
		
	}
	

	
	//private logic
	
	abstract class Abstract<SELF extends Abstract<SELF,S>,S extends Bean & Identified.Bean> extends Identified.Private<SELF,S> implements Attributed {


		public Abstract(S state) {
			
			super(state);

		}
		

		@Override
		public Container.Private<Attribute.Private,Attribute.Bean> attributes() {
			
			return new Container.Private<>(bean().attributes());
		
		}
		
		@Override
		public Date created() {
			
			String val = lookup(CREATION_TIME);
			
			try {
				return val==null? null : getDateTimeInstance().parse(val);
			}
			catch(ParseException e) {
				throw unchecked(e);
			}
		}
		
		@Override
		public Date lastUpdated() {
			
			String val = lookup(UPDATE_TIME);
			
			try {
				return val==null? created():getDateTimeInstance().parse(val);
			}
			catch(Exception e) {
				throw unchecked(e);
			}
		}
		
		@Override
		public String lastUpdatedBy() {
			
			return lookup(UPDATED_BY);
			
		}

		
		@Override
		public String originId() {
			
			return lookup(PREVIOUS_VERSION_ID);
			
		}
		
		
		public String lookup(Named def) {
			
			Collection<Attribute.Private> matches  = attributes().get(def);
			
			return matches.isEmpty() ? null : matches.iterator().next().value();
		
		}
	

		@Override
		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			attributes().update(changeset.attributes());
			
			//these may be materialised from storage, sacrifice legibility for minimal handling
			BeanContainer<Attribute.Bean> attributes = bean().attributes();
			
			if (timestampIn(attributes))
		
				updateTimestampAndUserIn(attributes);
				
			else
				
				addTimestampAndUserTo(attributes);
		

		}

		//helpers
		private boolean timestampIn(BeanContainer<Attribute.Bean> attributes) {
			return attributes.contains(UPDATE_TIME.qname());
		}
		
		private void updateTimestampAndUserIn(BeanContainer<Attribute.Bean> attributes) {
			attributes.getFirst(UPDATE_TIME.qname()).value(time());
			attributes.getFirst(UPDATED_BY.qname()).value(currentUser().name());
		}
		
		private void addTimestampAndUserTo(BeanContainer<Attribute.Bean> attributes) {
			attributes.add(stateof(attribute().instanceOf(UPDATE_TIME).value(time())));
			attributes.add(stateof(attribute().instanceOf(UPDATED_BY).value(currentUser().name())));
		}
		
	}
	
	
}
