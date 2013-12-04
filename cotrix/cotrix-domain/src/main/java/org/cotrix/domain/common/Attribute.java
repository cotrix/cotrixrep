package org.cotrix.domain.common;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;

/**
 * A named and typed attribute for a domain object.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Attribute extends Identified, Named {

	/**
	 * Returns the name of the attribute.
	 * 
	 * @return the name
	 */
	QName name();

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

	static interface State extends Attribute, Identified.State<Attribute.Private> {

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
	static class Private extends Identified.Abstract<Private> implements Attribute {

		private final Attribute.State state;

		/**
		 * Creates a new instance with a given state.
		 * 
		 * @param state the state
		 */
		public Private(Attribute.State state) {

			this.state = state;
		}

		@Override
		public Attribute.State state() {
			return state;
		}

		@Override
		public QName name() {
			return state.name();
		}

		@Override
		public QName type() {
			return state.type();
		}

		@Override
		public String value() {
			return state.value();
		}

		public void value(String value) {
			state.value(value);
		}

		@Override
		public String language() {
			return state.language();
		}

		protected void fillPO(AttributePO po) {
			po.name(name());
			po.type(type());
			po.value(value());
			if (language() != null)
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
					state.name(changeset.name());

			if (changeset.type() != null)
				state.type(changeset.type() == NULL_QNAME ? null : changeset.type());

			if (changeset.value() != null)
				state.value(changeset.value() == NULL_STRING ? null : changeset.value());

			if (changeset.language() != null)
				state.language(changeset.language() == NULL_STRING ? null : changeset.language());

		}

		@Override
		public String toString() {
			return "Attribute [id=" + id() + ", name=" + name() + ", value=" + value() + ", language=" + language()
					+ (type() == null ? "" : ", type=" + type()) + (status() == null ? "" : " (" + status() + ") ")
					+ "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Private))
				return false;
			Private other = (Private) obj;
			if (state == null) {
				if (other.state != null)
					return false;
			} else if (!state.equals(other.state))
				return false;
			return true;
		}
		
		

	}
	
	
}