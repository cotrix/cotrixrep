package org.cotrix.io.tabular.map;

/**
 * The mapping mode for a domain object.
 * <p>
 *  In {@link #strict} mode, missing values in the imported data produce no objects and generate errors. 
 *  In {@link #log} mode, they produce no objects and generate warnings. 
 *  In {@link #ignore} mode, they produce no objects and do not generate warnings. 
 *  
 * @author Fabio Simeoni
 *
 */
public enum MappingMode {

	strict,
	log,
	ignore

}