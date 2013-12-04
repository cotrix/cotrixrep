package org.cotrix.domain.common;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.memory.AttributeMS;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.EntityProvider;

/**
 * A named and typed attribute for a domain object.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Attribute extends Identified, Named {

	/**
	 * Returns the type of the attribute.
	 * 
	 * @return the type
	 */
	QName type();

	/**
	 * Returns the value of the attribute
	 * 
	 * @return the value
	 */
	String value();

	/**
	 * Returns the language of the attribute
	 * 
	 * @return the language
	 */
	String language();
	

	static interface State extends Attribute, Identified.State, EntityProvider<Private> {

		void name(QName type);

		void type(QName type);

		void value(String value);

		void language(String language);
	}

	static class Private extends Identified.Abstract<Private,State> implements Attribute {

		public Private(Attribute.State state) {
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
		public String value() {
			return state().value();
		}

		@Override
		public String language() {
			return state().language();
		}

		public Private copy(boolean withId) {
			
			AttributeMS state = new AttributeMS(withId ? id() : null);
			state.name(name());
			state.type(type());
			state.value(value());
			state.language(language());
			return new Private(state);
			
		}

		@Override
		public void update(Attribute.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			if (changeset.name() != null)
				if (changeset.name() == NULL_QNAME)
					throw new IllegalArgumentException("attribute name " + name() + " cannot be erased");
				else
					state().name(changeset.name());

			if (changeset.type() != null)
				state().type(changeset.type() == NULL_QNAME ? null : changeset.type());

			if (changeset.value() != null)
				state().value(changeset.value() == NULL_STRING ? null : changeset.value());

			if (changeset.language() != null)
				state().language(changeset.language() == NULL_STRING ? null : changeset.language());

		}

		@Override
		public String toString() {
			return "Attribute [id=" + id() + ", name=" + name() + ", value=" + value() + ", language=" + language()
					+ (type() == null ? "" : ", type=" + type()) + (status() == null ? "" : " (" + status() + ") ")
					+ "]";
		}


	}
	
	
}