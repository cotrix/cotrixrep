package org.cotrix.tabular.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.core.CotrixException;

public class UniquenessRule {

	/**
	 * multiply doubles with DECIMALS, round to int and muliply with DECIMALS
	 */
	public static final int DECIMALS = 100;
	public static final int DUPLICATES = 2;

	/**
	 * Given a number of columns, come up with a good spread of uniqueness
	 * numbers per dimension, in an unordered manner.
	 * 
	 * Uniqueness of 1 means every element is unique. Uniqueness of 0 means that
	 * all elements are the same.
	 * 
	 * The uniqueness values do appear twice in the list. This allows to have
	 * also 1 to 1 relations in the list.
	 * 
	 * 
	 * 
	 */

	public Double[] calculateUniqueness(int nrOfConcepts) {
		// precondition
		if (nrOfConcepts <= 0) {
			throw new CotrixException("At least give 1 dimension ");
		}

		List<Double> u = new ArrayList<Double>();
		double spacesWithDuplicates = (nrOfConcepts - 1) / DUPLICATES;
		// calculate the distance between the values.
		double distance = 1 / spacesWithDuplicates;

		// it always start at 1
		double pointer = 1;

		for (int i = 0; i < nrOfConcepts; i++) {
			u.add(new Double(pointer));
			if (u.size() < nrOfConcepts) {
				// this is causing columns with the same value (DUPCLICATES =2)
				u.add(new Double(pointer));
				i++;
			}

			// go the the next value
			pointer = pointer - distance;

			// workaround for the problem that something like 0.0000001 remains.
			pointer = Math.rint(pointer * DECIMALS) / DECIMALS;
		}

		if (nrOfConcepts != u.size()) {
			throw new CotrixException("Inconsistency detected, Number of Concepts is " + nrOfConcepts
					+ ", nr of calculated elements is " + u.size());
		}

		Double[] array = new Double[nrOfConcepts];
		return u.toArray(array);
	}

	/**
	 * Same as calculateUniqueness, except that the order is shuffled.
	 * 
	 * @param nrOfConcepts
	 * @return
	 */
	public Double[] calculateUniquenessRandom(int nrOfConcepts) {
		Double[] ordered = calculateUniqueness(nrOfConcepts);
		List<Double> random = new ArrayList<Double>();
		for (Double d : ordered) {
			random.add(d);
		}
		Collections.shuffle(random);
		Double[] array = new Double[nrOfConcepts];
		return random.toArray(array);
	}

	public int calculateNrOf1to1(int columns) {
		int calculateNrOf1to1 = 0;
		UniquenessRule u = new UniquenessRule();
		Double[] a = u.calculateUniqueness(columns);
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				if (i != j) {
					// System.out.println(a[i] + " " + a[j]);
					if (a[i].equals(a[j])) {
						calculateNrOf1to1++;
					}
				}
			}
		}
		return calculateNrOf1to1;
	}

	public int calculateNrOf1toN(int columns) {
		int calculateNrOf1toN = 0;
		UniquenessRule u = new UniquenessRule();
		Double[] a = u.calculateUniqueness(columns);
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				if (i != j) {
					if (a[i] > (a[j])) {
						calculateNrOf1toN++;
					}
				}
			}
		}
		return calculateNrOf1toN;
	}

	public int calculateNrOfUniqueColumns(int nrOfColumns) {
		int nrOf1to1Colums = 0;
		UniquenessRule u = new UniquenessRule();
		Double[] a = u.calculateUniqueness(nrOfColumns);
		for (Double spread : a) {
			if (spread == 1.0) {
				nrOf1to1Colums++;
			}
		}
		return nrOf1to1Colums;
	}

}
