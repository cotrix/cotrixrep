/**
 * 
 */
package org.cotrix.web.manage.shared.modify;

import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class ModifyCommandResult extends AbstractFeatureCarrier {
	
	public static class Void extends ModifyCommandResult {}

}
