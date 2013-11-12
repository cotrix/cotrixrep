package org.cotrix.repository.memory;

import static org.cotrix.domain.utils.Constants.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.namespace.QName;

import org.cotrix.common.Utils;
import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;

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
	@Inject
	public MCodelistRepository(IdGenerator generator) {
		this(new MStore(), generator);
	}

	/**
	 * Creates an instance over a given {@link MStore}.
	 * 
	 * @param store
	 */
	public MCodelistRepository(MStore store, IdGenerator generator) {
		super(store, Codelist.class, Codelist.Private.class, generator);
	}

	@Override
	public void add(Codelist list) {

		super.add(list);

		for (Attribute a : list.attributes())
			Utils.reveal(a, Attribute.Private.class).setId(generateId());

		for (CodelistLink l : list.links())
			Utils.reveal(l, CodelistLink.Private.class).setId(generateId());

		for (Code c : list.codes()) {
			for (Attribute a : c.attributes())
				Utils.reveal(a, Attribute.Private.class).setId(generateId());

			for (Codelink l : c.links())
				Utils.reveal(l, Codelink.Private.class).setId(generateId());

			Utils.reveal(c, Code.Private.class).setId(generateId());
		}

	}

	public void update(Codelist changeset) {

		for (Code code : changeset.codes()) {

			if (code.id() == null)
				Utils.reveal(code, Code.Private.class).setId(generateId());

			for (Attribute attr : code.attributes())
				if (attr.id() == null)
					Utils.reveal(attr, Attribute.Private.class).setId(generateId());

		}

		for (Attribute attr : changeset.attributes())
			if (attr.id() == null)
				Utils.reveal(attr, Attribute.Private.class).setId(generateId());

		super.update(changeset);

	};

	@Override
	public CodelistSummary summary(String id) {

		Codelist list = lookup(id);

		if (list == null)
			throw new IllegalStateException("no such codelist: " + id);

		int size = list.codes().size();

		Map<QName,Map<QName,Set<String>>> fingerprint = new HashMap<QName, Map<QName,Set<String>>>();
		
		for (Attribute a : list.attributes())
			addAttributeToFingerprint(fingerprint, a);
		
		for (Code c : list.codes())
			for (Attribute a : c.attributes())
				addAttributeToFingerprint(fingerprint, a);


		System.out.println(fingerprint);
		
		return new CodelistSummary(list.name(), size, fingerprint);
	}
	
	//helper
	private void addAttributeToFingerprint(Map<QName,Map<QName,Set<String>>> fingerprint,Attribute a) {
		
		if (a.type().equals(SYSTEM_TYPE))
			return;
		
		
		Map<QName,Set<String>> typeForAttr = fingerprint.get(a.name());
		
		if (typeForAttr==null) {
			
			typeForAttr = new HashMap<QName, Set<String>>();
			fingerprint.put(a.name(), typeForAttr);
			
		}
		
		Set<String> langForType = typeForAttr.get(a.type());
			
		if (langForType==null) {
			langForType = new HashSet<String>();
			typeForAttr.put(a.type(),langForType);	
		}
		
		if (a.language() != null)
			langForType.add(a.language());
		
	}
}
