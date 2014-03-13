/**
 * 
 */
package org.cotrix.web.common.server.task;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ANNOTATION_TYPE})
public @interface Task {

}
