/**
 * 
 */
package org.cotrix.web.common.server.async;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface Async {

}
