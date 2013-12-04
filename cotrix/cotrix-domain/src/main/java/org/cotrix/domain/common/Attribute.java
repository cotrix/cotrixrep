package org.cotrix.domain.common;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.po.AttributePO;
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

	/**
	 * {@link Attribute} implementation.
	 * 
	 * @author Fabio Simeoni
	 * 
	 */
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

		public void value(String value) {
		
			state().value(value);
		
		}

		@Override
		public String language() {
	
			return state().language();
		
		}

		protected void fillPO(AttributePO po) {
			
			po.name(name());
			po.type(type());
			po.value(value());
			po.language(language());
		}

		public Private copy(boolean withId) {
			AttributePO po = new AttributePO(withId ? id() : null);
			fillPO(po);
			return new Private(po);
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