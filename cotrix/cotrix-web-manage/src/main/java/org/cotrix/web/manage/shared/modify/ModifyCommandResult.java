/**
 * 
 */
package org.cotrix.web.manage.shared.modify;

import org.cotrix.web.common.shared.feature.FeatureCarrier;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class ModifyCommandResult extends FeatureCarrier {
	
	public static class Void extends ModifyCommandResult {}

}
