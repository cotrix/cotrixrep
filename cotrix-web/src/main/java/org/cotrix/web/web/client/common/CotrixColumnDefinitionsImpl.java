package org.cotrix.web.web.client.common;

//import com.google.gwt.user.client.ui.CheckBox;
//import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

import org.cotrix.web.shared.ContactDetails;

@SuppressWarnings("serial")
public class CotrixColumnDefinitionsImpl extends ArrayList<ColumnDefinition<ContactDetails>> {

	private static CotrixColumnDefinitionsImpl instance = null;

	public static CotrixColumnDefinitionsImpl getInstance() {
		if (instance == null) {
			instance = new CotrixColumnDefinitionsImpl();
		}

		return instance;
	}

	protected CotrixColumnDefinitionsImpl() {
		this.add(new ColumnDefinition<ContactDetails>() {
			public void render(ContactDetails c, StringBuilder sb) {
				sb.append("<input type='checkbox'/>");
			}

			public boolean isSelectable() {
				return true;
			}
		});

		this.add(new ColumnDefinition<ContactDetails>() {
			public void render(ContactDetails c, StringBuilder sb) {
				sb.append("<div id='" + c.getDisplayName() + "'>" + c.getDisplayName() + "</div>");
			}

			public boolean isClickable() {
				return true;
			}
		});
	}
}
