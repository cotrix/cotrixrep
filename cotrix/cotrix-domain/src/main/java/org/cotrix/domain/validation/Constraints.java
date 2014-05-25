package org.cotrix.domain.validation;

import static java.lang.String.*;

import java.util.Iterator;
import java.util.List;

public final class Constraints implements Iterable<Constraint> {

	private static String conditionTemplate = "(%s)";
	
	private final List<Constraint> constraints;
	
	public Constraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}
	
	@Override
	public Iterator<Constraint> iterator() {
		return constraints.iterator();
	}
	
	public Constraint asSingleConstraint() {
		
		return new Constraint("composition", compose());
	}

	
	//helper
	private String compose() {
		
		if (constraints.isEmpty())
			return "true"; //no constraint
		
		String first = String.format(conditionTemplate,constraints.get(0).expression());
		
		StringBuilder builder = new StringBuilder(first);
		
		for (int i =1; i<constraints.size(); i++)
			builder.append(" && ").append(format(conditionTemplate,constraints.get(i).expression()));
		
		return builder.toString();
	}
}
