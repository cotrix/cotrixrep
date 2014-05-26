/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attributetype;

import org.cotrix.web.common.client.widgets.EnumListBox.LabelProvider;
import org.cotrix.web.common.shared.codelist.attributetype.UIRange;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum Occurrences {
	
	CUSTOM("Custom", true, true),
	AT_LEAST("At least", true, false),
	AT_MOST("At most",0,Integer.MAX_VALUE, false,true),
	ONCE("Once",1,1),
	AT_MOST_ONCE("At most once",0,1),
	ARBITRARY("Arbitrary",0,Integer.MAX_VALUE),
	AT_LEAST_ONCE("At least once",1,Integer.MAX_VALUE);
	
	private String label;
	private int min;
	private int max;
	private boolean customMin;
	private boolean customMax;
	
	private Occurrences(String label, int min, int max) {
		this(label,min,max,false,false);
	}
	
	private Occurrences(String label, boolean customMin, boolean customMax) {
		this(label, Integer.MAX_VALUE, Integer.MAX_VALUE, customMin, customMax);
	}

	private Occurrences(String label, int min, int max, boolean customMin,
			boolean customMax) {
		this.label = label;
		this.min = min;
		this.max = max;
		this.customMin = customMin;
		this.customMax = customMax;
	}

	public String getLabel() {
		return label;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public boolean isCustomMin() {
		return customMin;
	}

	public boolean isCustomMax() {
		return customMax;
	}
	
	public UIRange toRange(int userMin, int userMax) {
		UIRange range = new UIRange();
		range.setMin(customMin?userMin:min);
		range.setMax(customMax?userMax:max);
		return range;
	}
	
	public static Occurrences toOccurrences(UIRange range) {
		//most restrictive first
		for (Occurrences occurrences:values()) {
			if (!occurrences.customMin && !occurrences.customMax) {
				if (!any(occurrences.min) && occurrences.min != range.getMin()) continue;
				if (any(occurrences.min) && !any(range.getMin())) continue;
				
				if (!any(occurrences.max) && occurrences.max != range.getMax()) continue;
				if (any(occurrences.max) && !any(range.getMax())) continue;
				return occurrences;
			}
		}
		
		//less restrictive then
		for (Occurrences occurrences:values()) {
			if ((occurrences.customMin || occurrences.customMax) && occurrences!=CUSTOM) {
				if (!occurrences.customMin && occurrences.min != range.getMin()) continue;
				if (!occurrences.customMax && occurrences.max != range.getMax()) continue;
				return occurrences;
			}
		}
		
		return Occurrences.CUSTOM;
	}
	
	private static boolean any(int value) {
		return value == Integer.MAX_VALUE;
	}
	
	public static LabelProvider<Occurrences> LABEL_PROVIDER = new LabelProvider<Occurrences>() {

		@Override
		public String getLabel(Occurrences item) {
			return item.getLabel();
		}
	};	

}
