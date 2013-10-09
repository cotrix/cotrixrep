package org.cotrix.web.share.client.widgets;

import java.util.EnumSet;

import com.google.gwt.user.client.ui.ListBox;

/**
 * A {@link ListBox} that contains values of an enum.
 * Partial source from https://code.google.com/p/ooo-gwt-utils/source/browse/trunk/src/main/java/com/threerings/gwt/ui/EnumListBox.java?r=206
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 * @param <E>
 */
public class EnumListBox<E extends Enum<E>> extends ListBox
{
	public interface LabelProvider<E> {
		public String getLabel(E item);
	}

	protected Class<E> _eclass;	

	/**
	 * Creates an enum list box that displays all values in the supplied enum.
	 */
	public EnumListBox(Class<E> eclass)
	{
		this(eclass, EnumSet.allOf(eclass), new LabelProvider<E>() {

			@Override
			public String getLabel(E item) {
				return String.valueOf(item);
			}
		});
	}

	public EnumListBox(Class<E> eclass, LabelProvider<E> labelProvider)
	{
		this(eclass, EnumSet.allOf(eclass), labelProvider);
	}

	/**
	 * Creates an enum list box that displays the values in the supplied set.
	 */
	public EnumListBox(Class<E> eclass, EnumSet<E> elements, LabelProvider<E> labelProvider)
	{
		_eclass = eclass;
		for (E value : elements) {
			String label = labelProvider.getLabel(value);
			addItem(label, value.toString());
		}
	}

	/**
	 * Selects the specified value.
	 */
	public void setSelectedValue(E value)
	{
		String valstr = value.toString();
		for (int ii = 0; ii < getItemCount(); ii++) {
			if (getValue(ii).equals(valstr)) {
				setSelectedIndex(ii);
				break;
			}
		}
	}

	/**
	 * Returns the currently selected value, or null if no value is selected.
	 */
	public E getSelectedValue()
	{
		int selidx = getSelectedIndex();
		return (selidx < 0) ? null : Enum.valueOf(_eclass, getValue(selidx));
	}
}