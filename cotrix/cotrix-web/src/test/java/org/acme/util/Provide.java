/**
 * 
 */
package org.acme.util;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Target({ FIELD}) @Retention(RUNTIME)
public @interface Provide {
	
	
}
