package org.cotrix.web.web.client.common;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.shared.ContactDetails;

public class CotrixColumnDefinitionsFactory<T> {
	public static List<ColumnDefinition<ContactDetails>> getCotrixColumnDefinitions() {
		return CotrixColumnDefinitionsImpl.getInstance();
	}

	public static List<ColumnDefinition<ContactDetails>> getTestCotrixColumnDefinitions() {
		return new ArrayList<ColumnDefinition<ContactDetails>>();
	}
}
