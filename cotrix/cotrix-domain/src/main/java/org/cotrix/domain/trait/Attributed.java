package org.cotrix.domain.trait;

import static java.text.DateFormat.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.text.ParseException;
import java.util.Date;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.common.Container;

/**
 * A domain object with {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Attributed {

	
	Container<? extends Attribute> attributes();
	
	
	Date created();
	
	Date lastUpdated();
	
	String lastUpdatedBy();
	
	String originId();
	
	QName originName();
	
	String valueOf(Named named);
	
	//----------------------------------------
	
	
	interface Bean extends Named.Bean {
		
		BeanContainer<Attribute.Bean> attributes();
		
	}
	

	//----------------------------------------
	
	
	abstract class Private<SELF extends Private<SELF,B>,B extends Bean> 
							
							extends Named.Private<SELF,B> 
							
							implements Attributed {

		
		public Private(B bean) {
			
			super(bean);
		}
		

		@Override
		public Container.Private<Attribute.Private,Attribute.Bean> attributes() {
			
			return new Container.Private<>(bean().attributes());
		
		}
		
		@Override
		public Date created() {
			
			String val = valueOf(CREATION_TIME);
			
			try {
				return val==null? null : getDateTimeInstance().parse(val);
			}
			catch(ParseException e) {
				throw unchecked(e);
			}
		}
		
		@Override
		public Date lastUpdated() {
			
			String val = valueOf(UPDATE_TIME);
			
			try {
				return val==null? created():getDateTimeInstance().parse(val);
			}
			catch(Exception e) {
				throw unchecked(e);
			}
		}
		
		@Override
		public String lastUpdatedBy() {
			
			return valueOf(UPDATED_BY);
			
		}

		
		@Override
		public String originId() {
			
			return valueOf(PREVIOUS_VERSION_ID);
			
		}
		
		@Override
		public QName originName() {
			
			String val = valueOf(PREVIOUS_VERSION_NAME);
			
			return val == null ? null: QName.valueOf(val);
			
		}
		
		
		@Override
		public String valueOf(Named def) {
			
			Attribute a  = attributes().getFirst(def);
			
			return a==null ? null : a.value();
		
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
			attributes.add(beanOf(attribute().instanceOf(UPDATE_TIME).value(time())));
			attributes.add(beanOf(attribute().instanceOf(UPDATED_BY).value(currentUser().name())));
		}
		
	}
	
	
}
