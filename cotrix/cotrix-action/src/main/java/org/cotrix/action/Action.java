package org.cotrix.action;

import java.util.Collection;
import java.util.List;

/**
 * An action that may be taken by the application over a given resource.
 * <p>
 * An action is comprised of:
 * 
 * <ul>
 * <li>a sequence of one ore more labels;
 * <li>a resource identifier;
 * <li>a {@link ResourceType}.
 * </ul>
 * 
 * Labels and identifiers are plain strings, with {@link #any} standing for any possible string. An action over
 * {@link #any} resource defines a <em>template</em>.<p>
 * 
 * The wildcard {@link #any} defines a notion of <em>inclusion</em> between actions: <code>A</code> is included in
 * <code>B</code> if the labels and resource identifier of <code>A</code> <em>match</em> those of <code>B</code>, where
 * a string matches only itself or {@link #any}.<p>
 * 
 * Note that any action is included in itself and equivalent actions (in the sense of {@link #equals(Object)}) are
 * included in each other;
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Action {

	/**
	 * The label and identifier wildcard.
	 */
	public static final String any = "*";
	

	/**
	 * Returns the type of this action.
	 * 
	 * @return the type
	 */
	ResourceType type();

	/**
	 * Returns the labels of this action.
	 * 
	 * @return the labels
	 */
	List<String> labels();

	/**
	 * Returns an action with the same labels as this action but on a given resource.
	 * 
	 * @param resource the resource identifier
	 * 
	 * @return the action with the same labels as this action but on the given resource
	 */
	Action on(String resource);

	/**
	 * Returns the identifier of the resource for this action.
	 * 
	 * @return the identifier of the resource
	 */
	String resource();

	/**
	 * Returns <code>true</code> if this action is a template.
	 * 
	 * @return <code>true</code> if this action is a template
	 */
	boolean isTemplate();

	/**
	 * Returns <code>true</code> if this action is included in at least one of a list of actions.
	 * 
	 * @param actions the actions
	 * @return <code>true</code> if this action is included in at least one of the actions in input.
	 */
	boolean included(Action... actions);

	/**
	 * Returns <code>true</code> if this action is included in at least one of a list of actions.
	 * 
	 * @param actions the actions
	 * @return <code>true</code> if this action is included in at least one of the actions in input.
	 */
	boolean included(Collection<? extends Action> actions);

}
