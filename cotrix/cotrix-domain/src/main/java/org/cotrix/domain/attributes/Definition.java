package org.cotrix.domain.attributes;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Range;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


/**
 * The type of an attribute.
 * 
 */
public interface Definition extends Identified, Named {

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
	ValueType valueType();
	
	/**
	 * Returns the occurrence range for instances.
	 * @return the range
	 */
	Range range();
	
	
		
	
	static interface State extends Identified.State, Named.State, EntityProvider<Private> {

		QName type();

		void type(QName type);
		
		String language();

		void language(String language);
		
		ValueType valueType();
		
		void valueType(ValueType type);
		
		Range range();
		
		void range(Range type);
	}

	/**
	 * A {@link Named.Abstract} implementation of {@link Definition}.
	 * 
	 */
	public class Private extends Identified.Abstract<Private,State> implements Definition {

		public Private(Definition.State state) {
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
		public ValueType valueType() {
			return state().valueType();
		}
		
		
		@Override
		public Range range() {
			return state().range();
		}

		@Override
		public void update(Definition.Private changeset) throws IllegalArgumentException, IllegalStateException {

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

			
			ValueType newtype = changeset.state().valueType();
			
			if (newtype!=null)
				state().valueType(newtype);
			
			
			Range newrange = changeset.state().range();
			
			if (newrange!=null)
				state().range(newrange);
		}

		@Override
		public String toString() {
			return "Attribute Definition [id=" + id() + ", name=" + name() + ", range=" + range() + ", valueType=" + valueType() + ", language=" + language()
					+ (type() == null ? "" : ", type=" + type()) + (status() == null ? "" : " (" + status() + ") ")
					+ "]";
		}
	}
}
