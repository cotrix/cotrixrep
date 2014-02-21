package org.cotrix.common.cdi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Stereotype;

@Retention(RUNTIME)
@Target({ TYPE})
@Inherited

@Alternative
@Priority(1)

@Stereotype
public @interface ByDefault {

}
