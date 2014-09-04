package org.cotrix.domain.attributes;

import static org.cotrix.domain.utils.DomainConstants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Definition;
import org.cotrix.domain.values.ValueType;



public interface AttributeDefinition extends Definition {

	QName type();
	
	boolean is(QName type);
	
	boolean is(Facet facet);

	String language();
	
	ValueType valueType();
	
	boolean isShared();
	
	
	//----------------------------------------
	
	static interface Bean extends Definition.Bean, BeanOf<Private> {

		QName type();

		void type(QName type);
		
		String language();

		void language(String language);
		
		ValueType valueType();
		
		void valueType(ValueType type);
		
		boolean isShared();
		
		boolean is(QName name);
		
		boolean is(Facet facet);
		
	}

	
	//----------------------------------------
	
	public class Private extends Definition.Private<Private,Bean> implements AttributeDefinition {

		public Private(AttributeDefinition.Bean bean) {
			super(bean);
		}
		
		@Override
		public boolean isShared() {
			return bean().isShared();
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
		}

		@Override
		public String toString() {
			return "def [id=" + id() + ", shared=" + isShared()+ ", name=" + qname() + ", range=" + range() + ", valueType=" + valueType() + ", language=" + language()
					+ (type() == null ? "" : ", type=" + type()) + (status() == null ? "" : " (" + status() + ") ")
					+ "]";
		}
	}
}
