package org.cotrix.application.impl.versioning;

import static org.cotrix.common.CommonUtils.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.action.events.CodelistActionEvents;
import org.cotrix.application.VersioningService;
import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;

/**
 * Default {@link VersioningService} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
@ApplicationScoped
public class DefaultVersioningService implements VersioningService {
	
	@Inject
	IdGenerator generator;
	
	@Inject
	Event< CodelistActionEvents.Version> events;
	
	@Inject @Current
	private BeanSession session;
	
	public <T extends Versioned & Identified & Named> VersioningService.VersionClause<T> bump(T object) {
		
		notNull("object", object);
		
		final Versioned.Abstract<?,?> versionable = reveal(object,Versioned.Abstract.class);
		
		return new VersionClause<T>() {
			
			@Override
			public T to(String version) {
				
				//delegates validation according to default version scheme
				Version v = new DefaultVersion(version);
				
				@SuppressWarnings("unchecked")
				T versioned = (T) versionable.bump(v.toString());
				
				events.fire(new CodelistActionEvents.Version(versionable.id(),versioned.id(),versioned.qname(),versioned.version(), session));
				
				return versioned;
			}
			
		};
	};
	
}
