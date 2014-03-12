/**
 * 
 */
package org.acme.util;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The field value will be used by Guice during binding.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Target({FIELD}) @Retention(RUNTIME)
public @interface Provide {
	Class<?> realType() default void.class;
}
