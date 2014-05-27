package org.cotrix.domain.events;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;


public interface Qualifiers {

	@Target({PARAMETER,FIELD})
	@Retention(RUNTIME)
	@Documented
	@Qualifier
	public static @interface New {}
	
	@Target({PARAMETER,FIELD})
	@Retention(RUNTIME)
	@Documented
	@Qualifier
	public static @interface Modified {}
	
	@Target({PARAMETER,FIELD})
	@Retention(RUNTIME)
	@Documented
	@Qualifier
	public static @interface Removed {}

}
