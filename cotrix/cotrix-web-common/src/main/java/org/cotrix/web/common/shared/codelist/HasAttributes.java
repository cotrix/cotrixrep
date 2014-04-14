/**
 * 
 */
package org.cotrix.web.common.shared.codelist;

import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface HasAttributes {
	public List<UIAttribute> getAttributes();
	public void setAttributes(List<UIAttribute> attributes);
}
