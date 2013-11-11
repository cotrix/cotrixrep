package org.cotrix.application.impl;

import static org.cotrix.common.Utils.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.application.VersioningService;
import org.cotrix.domain.spi.IdGenerator;
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
	
	public <T extends Versioned> VersioningService.VersionClause<T> bump(T object) {
		
		notNull("object", object);
		
		final Versioned.Abstract<?> versionable = reveal(object,Versioned.Abstract.class);
		
		return new VersionClause<T>() {
			
			@Override
			public T to(String version) {
				
				//delegates validation according to default version scheme
				Version v = new DefaultVersion(version);
				
				@SuppressWarnings("unchecked")
				T versioned = (T) versionable.bump(v.toString());
				
				return versioned;
			}
			
		};
	};
	
}
