package org.cotrix.domain.links;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ValueFunctions {
	
	public static final ValueFunction identity = new ValueFunction.Abstract("identity") {
		
		@Override
		public String apply(String value) {
			return value;
		}
	};
	
	public static final ValueFunction uppercase = new ValueFunction.Abstract("uppercase") {
		
		@Override
		public String apply(String value) {
			return value.toUpperCase();
		}
	};
	
	public final static ValueFunction lowercase = new ValueFunction.Abstract("lowercase") {
		
		@Override
		public String apply(String value) {
			return value.toLowerCase();
		}
	};
	
	public static PrefixFunction prefix(String prefix) {
		return new PrefixFunction(prefix);
	}
	
	public static class PrefixFunction extends ValueFunction.Abstract {
		
		private final String prefix;
		
		public PrefixFunction(String prefix) {
			super("prefix");
			this.prefix = prefix;
		}
		
		public String prefix() {
			return prefix;
		}
		
		@Override
		public String apply(String value) {
			return prefix+value;
		}
	}
	
	public static ValueFunction suffix(String suffix) {
		
		return new SuffixFunction(suffix);
	}
	
	public static class SuffixFunction extends ValueFunction.Abstract {
		
		private final String suffix;
		
		public SuffixFunction(String suffix) {
			super("suffix");
			this.suffix = suffix;
		}
		
		public String suffix() {
			return suffix;
		}
		
		@Override
		public String apply(String value) {
			return value+suffix;
		}
	}
	
	public static CustomFunction custom(String expression) {
		return new CustomFunction(expression);
	}
	
	
	
	public static class CustomFunction extends ValueFunction.Abstract {
		
		public static String VALUE_VAR = "$value";
		
		private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		
		private final String expression; 
				
		public CustomFunction(String expression) {
			super("generic");
			this.expression=expression;
		}
		
		public String expression() {
			return expression;
		}
		
		@Override
		public String apply(String value) {
			try {
				engine.put(VALUE_VAR, value);
				return (String) engine.eval(expression);
			}
			catch(Exception e) {
				throw new RuntimeException("cannot compute link value through generic function (see cause)",e);
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((expression == null) ? 0 : expression.hashCode());
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
			CustomFunction other = (CustomFunction) obj;
			if (expression == null) {
				if (other.expression != null)
					return false;
			} else if (!expression.equals(other.expression))
				return false;
			return true;
		}
		
		
	}

}
