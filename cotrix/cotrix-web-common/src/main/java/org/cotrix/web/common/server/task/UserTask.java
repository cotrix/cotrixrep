/**
 * 
 */
package org.cotrix.web.common.server.task;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.cotrix.action.UserAction;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
@Task
public @interface UserTask {
	
	UserAction value();

}
