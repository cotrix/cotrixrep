package org.cotrix.domain.coderelation;

import java.util.List;


/**
 * The relation container reflects the collection of relations between codes in
 * a certain space of codes.
 * 
 * The space of codes can be the original uploaded codelist or anything
 * whatsoever.
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class RelationContainer {

	protected List<Relations1to1> ListOf1to1Relations;
	protected List<Relations1toN> ListOf1toNRelations;

	public List<Relations1to1> getListOf1to1Relations() {
		return ListOf1to1Relations;
	}

	public void setListOf1to1Relations(List<Relations1to1> listOf1to1Relations) {
		ListOf1to1Relations = listOf1to1Relations;
	}

	public List<Relations1toN> getListOf1toNRelations() {
		return ListOf1toNRelations;
	}

	public void setListOf1toNRelations(List<Relations1toN> listOf1toNRelations) {
		ListOf1toNRelations = listOf1toNRelations;
	}

}
