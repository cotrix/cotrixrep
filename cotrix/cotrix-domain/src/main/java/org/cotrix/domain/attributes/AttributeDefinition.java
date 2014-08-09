package org.cotrix.domain.attributes;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Range;
import org.cotrix.domain.trait.Definition;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.values.ValueType;


/**
 * The definition of an attribute.
 * 
 */
public interface AttributeDefinition extends Definition {

	/**
	 * Returns the broad semantics of instances.
	 * 
	 * @return the type
	 */
	QName type();
	
	
	/**
	 * Returns <code>true</code> if this definition has a given type.
	 * @param type the type
	 * @return <code>true</code> if this definition supports the given type
	 */
	boolean is(QName type);
	
	/**
	 * Returns <code>true</code> if this definition supports a given facet.
	 * @param facet the facet
	 * @return <code>true</code> if this definition supports the given facet
	 */
	boolean is(Facet facet);

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
	static interface State extends Identified.Bean, Named.Bean, BeanOf<Private> {

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
		
		boolean is(Facet facet);
		
	}

	//private implementation: delegates to state bean
	public class Private extends Identified.Private<Private,State> implements AttributeDefinition {

		public Private(AttributeDefinition.State state) {
			super(state);
		}
		
		@Override
		public boolean isShared() {
			return bean().isShared();
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
		public boolean is(QName name) {
			return bean().is(name);
		}
		
		@Override
		public boolean is(Facet facet) {
			return bean().is(facet);
		}

		@Override
		public String language() {
			return bean().language();
		}

		@Override
		public ValueType valueType() {
			return bean().valueType();
		}
		
		
		
		@Override
		public Range range() {
			return bean().range();
		}

		@Override
		public void update(AttributeDefinition.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			//name (cannnot be reset)
			if (changeset.qname() == NULL_QNAME)
				throw new IllegalArgumentException("attribute name " + qname() + " cannot be erased");
			
			if (changeset.qname() != null)
				bean().qname(changeset.qname());

			//type (cannot be reset)
			if (changeset.type() == NULL_QNAME)
				throw new IllegalArgumentException("attribute type " + type() + " cannot be erased");
				
			if (changeset.type() != null)
				bean().type(changeset.type());

			
			//lang (can be reset)
			if (changeset.language() != null)
				bean().language(changeset.language() == NULL_STRING ? null : changeset.language());

			
			//value type
			ValueType newtype = changeset.valueType();
			
			if (newtype!=null)
				bean().valueType(newtype);
			
			//range
			Range newrange = changeset.range();
			
			if (newrange!=null)
				bean().range(newrange);
		}

		@Override
		public String toString() {
			return "def [id=" + id() + ", shared=" + isShared()+ ", name=" + qname() + ", range=" + range() + ", valueType=" + valueType() + ", language=" + language()
					+ (type() == null ? "" : ", type=" + type()) + (status() == null ? "" : " (" + status() + ") ")
					+ "]";
		}
	}
}
