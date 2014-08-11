package org.cotrix.domain.attributes;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


public interface Attribute extends Identified, Named, Defined<AttributeDefinition> {


	QName type();
	
	boolean is(QName type);
	
	boolean is(Facet facet);
	
	String value();

	String language();
	
	String description();
	

	//----------------------------------------
	
	interface Bean extends Identified.Bean, Named.Bean, BeanOf<Private> {

		
		AttributeDefinition.Bean definition();
		
		void definition(AttributeDefinition.Bean definition);
		
		
		QName type();
		void type(QName type);

		String value();
		void value(String value);

		String language();
		void language(String language);
		
		String description();
		void description(String description);
		
		boolean is(QName type);
		
		boolean is(Facet facet);
	}

	
	//----------------------------------------
	
	final class Private extends Identified.Private<Private,Bean> implements Attribute {

		public Private(Attribute.Bean state) {
			super(state);
		}
		
		@Override
		public AttributeDefinition definition() {
			return bean().definition().entity();
		}

		@Override
		public QName qname() {
			return bean().qname();
		}

		@Override
		public QName type() {
			return bean().type();
		}
		
		@Override
		public boolean is(QName type) {
			return bean().is(type);
		}
		
		@Override
		public boolean is(Facet facet) {
			return bean().is(facet);
		}
		
		@Override
		public String value() {
			return bean().value();
		}

		@Override
		public String language() {
			return bean().language();
		}
		
		@Override
		public String description() {
			return bean().description();
		}

		@Override
		public void update(Attribute.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			//TODO keep temporarily for retro-compatibility, update should occur at definition level.
			
			if (changeset.value() != null)
				bean().value(changeset.value() == NULL_STRING ? null : changeset.value());
				
			if (changeset.qname() == NULL_QNAME)
				throw new IllegalArgumentException("attribute name " + qname() + " cannot be erased");
			
			if (changeset.qname() != null)
				bean().qname(changeset.qname());

			if (changeset.type() == NULL_QNAME)
				throw new IllegalArgumentException("attribute type " + type() + " cannot be erased");
			
			if (changeset.type() != null)
				bean().type(changeset.type());

			if (changeset.language() != null)
				bean().language(changeset.language() == NULL_STRING ? null : changeset.language());
			
			if (changeset.description() != null)
				bean().description(changeset.description() == NULL_STRING ? null : changeset.description());

		}
		
		@Override
		public String toString() {
			return "attr [id=" + id() + ", value=" + value() + ", def=" + definition() + (status() == null ? "" : " (" + status() + ") ")
					+ "]";
		}

		
		//helper
		
//		private static final String validationErrorMsg = "%s cannot be assigned to attribute %s, as violates  constraint %s";
		
		
//		private String validateAndUpdateValue(String value) {
//			
//			ValueType type = definition().valueType();
//			
//			if (!type.isValid(value))
//				throw new IllegalArgumentException(format(validationErrorMsg, value, qname(),type.constraints().asSingleConstraint()));
//		
//			return value;
//		}


	}
	
	
	
	
}