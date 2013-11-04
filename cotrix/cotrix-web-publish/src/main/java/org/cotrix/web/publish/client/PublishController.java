/**
 * 
 */
package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.event.CodeListSelectedEvent;
import org.cotrix.web.publish.client.event.DestinationType;
import org.cotrix.web.publish.client.event.DestinationTypeChangeEvent;
import org.cotrix.web.publish.client.event.FormatType;
import org.cotrix.web.publish.client.event.FormatTypeChangeEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.shared.codelist.UICodelist;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishController {
	
	protected EventBus eventBus;
	
	
	protected UICodelist codelist;
	protected DestinationType destination;
	protected FormatType type;
	
	@Inject
	public PublishController(@PublishBus EventBus eventBus) {
		this.eventBus = eventBus;
		bind();
	}
	
	protected void bind()
	{
		eventBus.addHandler(CodeListSelectedEvent.TYPE, new CodeListSelectedEvent.CodeListSelectedHandler() {
			
			@Override
			public void onCodeListSelected(CodeListSelectedEvent event) {
				codelist = event.getSelectedCodelist();
			}
		});
		
		eventBus.addHandler(DestinationTypeChangeEvent.TYPE, new DestinationTypeChangeEvent.DestinationTypeChangeHandler() {
			
			@Override
			public void onDestinationTypeChange(DestinationTypeChangeEvent event) {
				destination = event.getDestinationType();
			}
		});
		
		eventBus.addHandler(FormatTypeChangeEvent.TYPE, new FormatTypeChangeEvent.FormatTypeChangeHandler() {
			
			@Override
			public void onFormatTypeChange(FormatTypeChangeEvent event) {
				type = event.getFormatType();
			}
		});
	}
	
	protected void reset()
	{
		codelist = null;
		type = null;
		destination = null;
		
		eventBus.fireEvent(new ResetWizardEvent());
	}

}
