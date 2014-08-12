package org.cotrix.application.impl.versioning;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Data.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.action.events.CodelistActionEvents;
import org.cotrix.application.VersioningService;
import org.cotrix.common.BeanSession;
import org.cotrix.common.events.Current;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.DefaultVersion;
import org.cotrix.domain.codelist.Version;

/**
 * Default {@link VersioningService} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
@ApplicationScoped
public class DefaultVersioningService implements VersioningService {
	

	@Inject
	Event< CodelistActionEvents.Version> events;
	
	@Inject @Current
	private BeanSession session;
	
	public VersioningService.VersionClause bump(Codelist list) {
		
		notNull("object", list);
		
		final Codelist.Private plist = reveal(list);
		
		return new VersionClause() {
			
			@Override
			public Codelist to(String version) {
				
				//delegates validation according to default version scheme
				Version v = new DefaultVersion(version);
				
				Codelist versioned = plist.bump(v.toString());
				
				events.fire(new CodelistActionEvents.Version(plist.id(),versioned.id(),versioned.qname(),versioned.version(), session));
				
				return versioned;
			}
			
		};
	};
	
}
