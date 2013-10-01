/**
 * 
 */
package org.cotrix.web.share.server.task;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.cotrix.action.CodelistAction;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Task {
	
	CodelistAction value();

}
