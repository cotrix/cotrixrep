package org.cotrix.domain.validation;

import static org.cotrix.domain.utils.ScriptEngineProvider.*;

import java.util.Map;

public final class Constraint {

	
	private final String expression;
	private final String name;
	private final Map<String,String> params;
	
	public Constraint(String name, String expression, Map<String,String> params) {
		this.expression=expression;
		this.name=name;
		this.params=params;
	}
	
	public boolean isMetBy(String value) {
		return Boolean.valueOf(engine().eval(expression).with(value));
	}
	
	public String name() {
		return name;
	}
	
	public String expression() {
		return expression;
	}
	
	public Map<String,String> params() {
		return params;
	}
	
	@Override
	public String toString() {
		return expression;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Constraint other = (Constraint) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
