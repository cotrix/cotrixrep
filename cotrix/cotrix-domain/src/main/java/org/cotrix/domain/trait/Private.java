package org.cotrix.domain.trait;



/**
 * The interface of all private implementations.
 * 
 * @author Fabio Simeoni
 *
 * @param <SELF> the type of the implementation
 */
public interface Private<SELF extends Private<SELF>> extends Mutable<SELF>,Copyable<SELF> {

}
