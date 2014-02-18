/**
 * 
 */
package org.cotrix.web.wizard.client.step;

import java.util.Iterator;
import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class WizardSteps {

	public static String toString(List<WizardStep> steps) {
		if (steps.isEmpty()) return "[]";
		Iterator<WizardStep> i = steps.iterator();

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (;;) {
			WizardStep e = i.next();
			sb.append(e.getId());
			if (! i.hasNext())
				return sb.append(']').toString();
			sb.append("-> ");
		}
	}

}
