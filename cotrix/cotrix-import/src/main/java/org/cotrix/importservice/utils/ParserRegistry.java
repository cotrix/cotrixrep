package org.cotrix.importservice.utils;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.cotrix.importservice.DefaultImportService;

/**
 * Qualifies the configuration of the {@link DefaultImportService} for injection.
 * 
 * @author Fabio Simeoni
 *
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD,PARAMETER})
public @interface ParserRegistry {

}
