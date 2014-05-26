package org.cotrix.domain.attributes;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static org.cotrix.domain.utils.ScriptEngineProvider.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.validation.Constraint;
import org.cotrix.domain.values.ValueType;

public class DefaultType implements ValueType {
	
	private static String conditionTemplate = "(%s)";
	
	private boolean required;
	private List<Constraint> constraints = new ArrayList<>();
	private String dflt = "";
	
	private String constraint = "true"; //no constraint

	//subclasses can choose their own default
	public DefaultType(boolean defaultRequired) {
		required = defaultRequired;
	}
	
	public DefaultType() {
		this(false);
	}
	
	public DefaultType required() {
		this.required = true;
		return this;
	}
	
	public DefaultType defaultsTo(String dflt) {
		this.dflt=dflt;
		return this;
	}
	
	@Override
	public String defaultValue() {
		return dflt;
	}
	
	public DefaultType with(List<Constraint> constraints) {
		this.constraints.addAll(constraints);
		this.constraint=compose(this.constraints);
		return this;
	}
	
	public DefaultType with(Constraint ... constraints) {
		return with(asList(constraints));
	}
	
	@Override
	public boolean isRequired() {
		return required;
	}
	
	@Override
	public String constraint() {
		return constraint;
	}
	
	@Override
	public List<Constraint> constraints() {
		return constraints;
	}
	
	@Override
	public boolean isValid(String value) {
		return Boolean.valueOf(engine().eval(constraint).with(value));
	}
	
	//helper
	private String compose(List<Constraint> constraints) {
		
		String first = String.format(conditionTemplate,constraints.get(0).expression());
		
		StringBuilder builder = new StringBuilder(first);
		
		for (int i =1; i<constraints.size(); i++)
			builder.append(" && ").append(format(conditionTemplate,constraints.get(i).expression()));
		
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constraints == null) ? 0 : constraints.hashCode());
		result = prime * result + ((dflt == null) ? 0 : dflt.hashCode());
		result = prime * result + (required ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultType other = (DefaultType) obj;
		if (constraints == null) {
			if (other.constraints != null)
				return false;
		} else if (!constraints.equals(other.constraints))
			return false;
		if (dflt == null) {
			if (other.dflt != null)
				return false;
		} else if (!dflt.equals(other.dflt))
			return false;
		if (required != other.required)
			return false;
		return true;
	}

	
	
	
}
