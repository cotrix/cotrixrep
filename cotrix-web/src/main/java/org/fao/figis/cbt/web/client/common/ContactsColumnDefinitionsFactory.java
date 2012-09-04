package org.fao.figis.cbt.web.client.common;

import java.util.ArrayList;
import java.util.List;

import org.fao.figis.cbt.shared.ContactDetails;

public class ContactsColumnDefinitionsFactory<T> {
  public static List<ColumnDefinition<ContactDetails>> 
      getContactsColumnDefinitions() {
    return ContactsColumnDefinitionsImpl.getInstance();
  }

  public static List<ColumnDefinition<ContactDetails>>
      getTestContactsColumnDefinitions() {
    return new ArrayList<ColumnDefinition<ContactDetails>>();
  }
}
