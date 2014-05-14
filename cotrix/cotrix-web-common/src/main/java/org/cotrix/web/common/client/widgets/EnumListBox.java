package org.cotrix.web.common.client.widgets;

import java.util.EnumSet;

import com.google.gwt.user.client.ui.ListBox;

/**
 * A {@link ListBox} that contains predefinedUsers of an enum.
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

	protected Class<E> enumClass;	

	/**
	 * Creates an enum list box that displays all predefinedUsers in the supplied enum.
	 */
	public EnumListBox(Class<E> enumClass)
	{
		this(enumClass, EnumSet.allOf(enumClass), new LabelProvider<E>() {

			@Override
			public String getLabel(E item) {
				return String.valueOf(item);
			}
		});
	}

	public EnumListBox(Class<E> enumClass, LabelProvider<E> labelProvider)
	{
		this(enumClass, EnumSet.allOf(enumClass), labelProvider);
	}

	/**
	 * Creates an enum list box that displays the predefinedUsers in the supplied set.
	 */
	public EnumListBox(Class<E> enumClass, EnumSet<E> elements, LabelProvider<E> labelProvider)
	{
		this.enumClass = enumClass;
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
		String valueString = value.toString();
		for (int i = 0; i < getItemCount(); i++) {
			if (getValue(i).equals(valueString)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

	/**
	 * Returns the currently selected value, or null if no value is selected.
	 */
	public E getSelectedValue()
	{
		int selected = getSelectedIndex();
		return (selected < 0) ? null : Enum.valueOf(enumClass, getValue(selected));
	}
}