package org.cotrix.io.tabular;

/**
 * The mapping mode for a domain object.
 * <p>
 *  In {@link #STRICT} mode, missing values in the imported data produce no objects and generate errors. 
 *  In {@link #LOG} mode, they produce no objects and generate warnings. 
 *  In {@link #IGNORE} mode, they produce no objects and do not generate warnings. 
 * @author Fabio Simeoni
 *
 */
public enum MappingMode {STRICT,LOG,IGNORE}