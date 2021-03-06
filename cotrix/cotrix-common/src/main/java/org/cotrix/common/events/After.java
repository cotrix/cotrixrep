package org.cotrix.common.events;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Target({PARAMETER})
@Retention(RUNTIME)
@Documented
@Qualifier
public @interface After {}