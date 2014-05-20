package org.cotrix.domain.common;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


/**
 * The type of an attribute.
 * 
 */
public interface AttributeType extends Identified, Named {

	/**
	 * Returns a name for the broad semantics of instances.
	 * 
	 * @return the type
	 */
	QName type();

	/**
	 * Returns the language of the instance values.
	 * 
	 * @return the language
	 */
	String language();
	
	/**
	 * Returns the data type of instance values.
	 * 
	 * @return the data type
	 */
	AttributeValueType valueType();
	
	/**
	 * Returns the occurrence range for instances.
	 * @return the range
	 */
	OccurrenceRange range();
	
	
		
	
	static interface State extends Identified.State, Named.State, EntityProvider<Private> {

		QName type();

		void type(QName type);
		
		String language();

		void language(String language);
		
		AttributeValueType valueType();
		
		void valueType(AttributeValueType type);
		
		OccurrenceRange range();
		
		void range(OccurrenceRange type);
	}

	/**
	 * A {@link Named.Abstract} implementation of {@link AttributeType}.
	 * 
	 */
	public class Private extends Identified.Abstract<Private,State> implements AttributeType {

		public Private(AttributeType.State state) {
			super(state);
		}
		
		@Override
		public QName name() {
			return state().name();
		}
		
		@Override
		public QName type() {
			return state().type();
		}

		@Override
		public String language() {
			return state().language();
		}

		@Override
		public AttributeValueType valueType() {
			return state().valueType();
		}
		
		
		@Override
		public OccurrenceRange range() {
			return state().range();
		}

		@Override
		public void update(AttributeType.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			if (changeset.name() != null)
				if (changeset.name() == NULL_QNAME)
					throw new IllegalArgumentException("attribute name " + name() + " cannot be erased");
				else
					state().name(changeset.name());

			if (changeset.type() != null)
				state().type(changeset.type() == NULL_QNAME ? null : changeset.type());

			if (changeset.language() != null)
				state().language(changeset.language() == NULL_STRING ? null : changeset.language());

			
			AttributeValueType newtype = changeset.state().valueType();
			
			if (newtype!=null)
				state().valueType(newtype);
			
			
			OccurrenceRange newrange = changeset.state().range();
			
			if (newrange!=null)
				state().range(newrange);
		}

	}
}
