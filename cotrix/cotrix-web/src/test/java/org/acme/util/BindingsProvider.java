/**
 * 
 */
package org.acme.util;

import java.util.List;

import org.acme.util.TestModule.Binding;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface BindingsProvider {
	
	public List<Binding> getBindings();

}
