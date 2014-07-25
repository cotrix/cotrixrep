/**
 * 
 */
package org.cotrix.web.manage.shared;

import org.cotrix.web.common.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum ManagerUIFeature implements UIFeature {

	VIEW_CODELIST,
	LOCK_CODELIST,
	UNLOCK_CODELIST,
	SEAL_CODELIST,
	UNSEAL_CODELIST,
	EDIT_CODELIST,
	EDIT_METADATA,
	VIEW_METADATA,
	REMOVE_LOGBOOKENTRY,
	ADD_CODE,
	REMOVE_CODE;
}
