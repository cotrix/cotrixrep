/**
 * 
 */
package org.cotrix.web.common.shared.codelist.linktype;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIValueFunction implements IsSerializable {
	
	public enum Function {
		IDENTITY,
		UPPERCASE,
		LOWERCASE,
		PREFIX("Prefix"),
		SUFFIX("Suffix"),
		CUSTOM("Expression");
		
		private String[] arguments;

		private Function(String ... arguments) {
			this.arguments = arguments;
		}

		/**
		 * @return the arguments
		 */
		public String[] getArguments() {
			return arguments;
		}		
	}
	
	private Function function;
	private List<String> arguments;
	
	public UIValueFunction() {
	}
	
	/**
	 * @param function
	 * @param arguments
	 */
	public UIValueFunction(Function function, List<String> arguments) {
		this.function = function;
		this.arguments = arguments;
	}
	
	/**
	 * @return the function
	 */
	public Function getFunction() {
		return function;
	}
	
	/**
	 * @return the arguments
	 */
	public List<String> getArguments() {
		return arguments;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UIValueFunction other = (UIValueFunction) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (function != other.function)
			return false;
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UIValueFunction [function=");
		builder.append(function);
		builder.append(", arguments=");
		builder.append(arguments);
		builder.append("]");
		return builder.toString();
	}
}
