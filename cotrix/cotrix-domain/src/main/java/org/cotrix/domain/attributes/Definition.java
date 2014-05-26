package org.cotrix.domain.attributes;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Range;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.values.ValueType;


/**
 * The definition of an attribute.
 * 
 */
public interface Definition extends Identified, Named {

	/**
	 * Returns the broad semantics of instances.
	 * 
	 * @return the type
	 */
	QName type();

	/**
	 * Returns the natural language of instance values.
	 * 
	 * @return the language, or <code>null</code> if instance values are in no natural language
	 */
	String language();
	
	/**
	 * Returns the data type of instance values.
	 * 
	 * @return the data type
	 */
	ValueType valueType();
	
	/**
	 * Returns the range with which instances can occur in context.
	 * @return the range
	 */
	Range range();
	
	
		
	//state interface
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

	//private implementation: delegates to state bean
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
			
			//name (cannnot be reset)
			if (changeset.name() == NULL_QNAME)
				throw new IllegalArgumentException("attribute name " + name() + " cannot be erased");
			
			if (changeset.name() != null)
				state().name(changeset.name());

			//type (cannot be reset)
			if (changeset.type() == NULL_QNAME)
				throw new IllegalArgumentException("attribute type " + type() + " cannot be erased");
				
			if (changeset.type() != null)
				state().type(changeset.type());

			
			//lang (can be reset)
			if (changeset.language() != null)
				state().language(changeset.language() == NULL_STRING ? null : changeset.language());

			
			//value type
			ValueType newtype = changeset.valueType();
			
			if (newtype!=null)
				state().valueType(newtype);
			
			//range
			Range newrange = changeset.range();
			
			if (newrange!=null)
				state().range(newrange);
		}

		@Override
		public String toString() {
			return "def [id=" + id() + ", name=" + name() + ", range=" + range() + ", valueType=" + valueType() + ", language=" + language()
					+ (type() == null ? "" : ", type=" + type()) + (status() == null ? "" : " (" + status() + ") ")
					+ "]";
		}
	}
}
