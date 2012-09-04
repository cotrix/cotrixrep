package org.fao.figis.cbt.web.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ContactUpdatedEventHandler extends EventHandler{
  void onContactUpdated(ContactUpdatedEvent event);
}
