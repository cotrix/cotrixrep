package org.cotrix.common.cdi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Target({ TYPE, METHOD, PARAMETER, FIELD })
@Retention(RUNTIME)
@Documented
@Qualifier
/**
 * A CDI qualifier to qualify beans produced by producer methods and separate them from those that can be instantiated from classes
 * 
 * @author Fabio Simeoni
 *
 */
@interface Produced {}