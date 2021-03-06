/**
 * 
 */
package org.cotrix.web.common.shared.codelist;

import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface Definition extends IsSerializable {

	String getId();
	UIRange getRange();
}
