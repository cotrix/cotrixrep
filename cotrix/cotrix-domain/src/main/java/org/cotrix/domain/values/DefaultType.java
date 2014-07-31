package org.cotrix.domain.values;

import static java.util.Arrays.*;
import static org.cotrix.common.script.ScriptEngineProvider.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.validation.Constraint;
import org.cotrix.domain.validation.Constraints;

public final class DefaultType implements ValueType {
	
	private boolean required;
	private List<Constraint> constraints = new ArrayList<>();
	private String dflt = null;

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
		return this;
	}
	
	public DefaultType with(Constraint ... constraints) {
		return with(asList(constraints));
	}
	
	@Override
	public Constraints constraints() {
		return new Constraints(constraints);
	}
	
	@Override
	public boolean isValid(String value) {
		return Boolean.valueOf(engine().eval(constraints().asSingleConstraint().expression()).with(value));
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
