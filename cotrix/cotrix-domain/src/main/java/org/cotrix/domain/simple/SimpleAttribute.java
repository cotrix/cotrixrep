package org.cotrix.domain.simple;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.pos.AttributePO;
import org.cotrix.domain.utils.IdGenerator;


/**
 * Default {@link Attribute} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleAttribute extends SimpleObject<Attribute> implements Attribute {

	private QName type;
	private String value;
	
	public SimpleAttribute(AttributePO params) throws IllegalArgumentException {
		
		super(params);
		
		this.type=params.type();
		this.value=params.value();
	}
	
	public QName type() {
		return type;
	}

	public String value() {
		return value;
	}
	
	protected void buildPO(AttributePO po) {
		super.buildPO(po);
		po.setType(type());
		po.setValue(value());
	}
	
	@Override
	public SimpleAttribute copy(IdGenerator generator) {
		AttributePO po = new AttributePO(generator.generateId());
		buildPO(po);
		return new SimpleAttribute(po);
	}
	
	@Override
	public void update(Attribute delta) throws IllegalArgumentException, IllegalStateException {
		
		super.update(delta);
		
		type=delta.type();
		
		value=delta.value();
		
	}

	@Override
	public String toString() {
		return "Attribute [id=" +id()+", name=" + name() + ", value=" + value + (type==null?"":", type=" + type)+ (change()==null?"":"("+change()+")")+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleAttribute other = (SimpleAttribute) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	
}