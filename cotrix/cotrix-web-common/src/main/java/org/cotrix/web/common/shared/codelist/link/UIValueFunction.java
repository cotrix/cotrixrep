/**
 * 
 */
package org.cotrix.web.common.shared.codelist.link;

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
		PREFIX("prefix"),
		SUFFIX("suffix"),
		CUSTOM("expression");
		
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
