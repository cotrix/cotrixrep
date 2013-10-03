package org.cotrix.repository.memory;

import java.util.UUID;

import javax.inject.Singleton;

import org.cotrix.common.Utils;
import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.repository.CodelistRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
@Singleton
public class MCodelistRepository extends MRepository<Codelist, Codelist.Private> implements CodelistRepository {

	/**
	 * Creates an instance over a private {@link MStore}.
	 */
	public MCodelistRepository() {
		this(new MStore());
	}
	
	/**
	 * Creates an instance over a given {@link MStore}.
	 * @param store
	 */
	public MCodelistRepository(MStore store) {
		super(store,Codelist.class,Codelist.Private.class);
	}
	
	@Override
	public void add(Codelist list) {
		
		super.add(list);
		
		
		for (Attribute a: list.attributes())
			Utils.reveal(a,Attribute.Private.class).setId(UUID.randomUUID().toString());
		
		for (CodelistLink l: list.links())
			Utils.reveal(l,CodelistLink.Private.class).setId(UUID.randomUUID().toString());
		
		for (Code c: list.codes()) {
			for (Attribute a: c.attributes())
				Utils.reveal(a,Attribute.Private.class).setId(UUID.randomUUID().toString());
			
			for (Codelink l: c.links())
				Utils.reveal(l,Codelink.Private.class).setId(UUID.randomUUID().toString());
			
			Utils.reveal(c,Code.Private.class).setId(UUID.randomUUID().toString());
		}
			
	}
	
}
