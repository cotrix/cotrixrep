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
public interface AttributeDefinition extends Identified, Named {

	/**
	 * Returns the broad semantics of instances.
	 * 
	 * @return the type
	 */
	QName type();
	
	
	boolean is(QName name);

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
	
	
	/**
	 * Returns <code>true</code> if this definition is shared by many instances.
	 * @return  <code>true</code> if this definition is shared by many instances
	 */
	boolean isShared();
	
	
		
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
		
		boolean isShared();
		
		boolean is(QName name);
		
	}

	//private implementation: delegates to state bean
	public class Private extends Identified.Abstract<Private,State> implements AttributeDefinition {

		public Private(AttributeDefinition.State state) {
			super(state);
		}
		
		@Override
		public boolean isShared() {
			return state().isShared();
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
		public void update(AttributeDefinition.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			//name (cannnot be reset)
			if (changeset.qname() == NULL_QNAME)
				throw new IllegalArgumentException("attribute name " + qname() + " cannot be erased");
			
			if (changeset.qname() != null)
				state().name(changeset.qname());

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
			return "def [id=" + id() + ", shared=" + isShared()+ ", name=" + qname() + ", range=" + range() + ", valueType=" + valueType() + ", language=" + language()
					+ (type() == null ? "" : ", type=" + type()) + (status() == null ? "" : " (" + status() + ") ")
					+ "]";
		}
	}
}
