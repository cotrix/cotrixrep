package org.cotrix.domain.attributes;

import static java.lang.String.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.values.ValueType;

/**
 * A descriptive attribute for a domain object.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Attribute extends Identified, Named, Defined<AttributeDefinition> {

	//public read-only interface
	
	/**
	 * Returns the broad semantics of this attribute.
	 * 
	 * @return the type
	 */
	QName type();

	
	boolean is(QName name);
	
	/**
	 * Returns the current value of this attribute
	 * 
	 * @return the value, <code>null</code> if the attribute has no current value.
	 */
	String value();

	
	/**
	 * Returns the language of this attribute's value
	 * 
	 * @return the language
	 */
	String language();
	
	
	/**
	 * Returns the description of this attribute
	 * 
	 * @return the language
	 */
	String description();
	

	//private state-based interface
	
	interface State extends Identified.State, Named.State, EntityProvider<Private> {

		AttributeDefinition.State definition();
		
		void definition(AttributeDefinition.State definition);
		
		
		QName type();

		String value();

		String language();
		
		String description();


		
		void type(QName type);
		void value(String value);
		void language(String language);
		void description(String description);
		
		boolean is(QName name);
	}

	
	//private implementation: delegates to state bean
	
	final class Private extends Identified.Abstract<Private,State> implements Attribute {

		private static final String validationErrorMsg = "%s cannot be assigned to attribute %s, as violates  constraint %s";
		

		public Private(Attribute.State state) {
			super(state);
		}
		
		@Override
		public AttributeDefinition definition() {
			return state().definition().entity();
		}

		@Override
		public QName qname() {
			return state().name();
		}

		@Override
		public QName type() {
			return state().type();
		}
		
		@Override
		public boolean is(QName name) {
			return state().is(name);
		}

		@Override
		public String value() {
			return state().value();
		}

		@Override
		public String language() {
			return state().language();
		}
		
		@Override
		public String description() {
			return state().description();
		}

		@Override
		public void update(Attribute.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			//TODO keep temporarily for retro-compatibility, update should occur at definition level.
			
			if (changeset.value() != null)
				state().value(changeset.value() == NULL_STRING ? null : validateAndUpdateValue(changeset.value()));
				
			if (changeset.qname() == NULL_QNAME)
				throw new IllegalArgumentException("attribute name " + qname() + " cannot be erased");
			
			if (changeset.qname() != null)
				state().name(changeset.qname());

			if (changeset.type() == NULL_QNAME)
				throw new IllegalArgumentException("attribute type " + type() + " cannot be erased");
			
			if (changeset.type() != null)
				state().type(changeset.type());

			if (changeset.language() != null)
				state().language(changeset.language() == NULL_STRING ? null : changeset.language());
			
			if (changeset.description() != null)
				state().description(changeset.description() == NULL_STRING ? null : changeset.description());

		}
		
		@Override
		public String toString() {
			return "attr [id=" + id() + ", value=" + value() + ", def=" + definition() + (status() == null ? "" : " (" + status() + ") ")
					+ "]";
		}

		
		//helper
		private String validateAndUpdateValue(String value) {
			
			ValueType type = definition().valueType();
			
			if (!type.isValid(value))
				throw new IllegalArgumentException(format(validationErrorMsg, value, qname(),type.constraints().asSingleConstraint()));
		
			return value;
		}


	}
	
	
	
	
}