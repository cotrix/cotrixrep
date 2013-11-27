package org.cotrix.repository.codelist.impl;

import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.repository.codelist.CodelistCoordinates.*;
import static org.cotrix.repository.codelist.CodelistSummary.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.trait.Named;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.codelist.CodelistCoordinates;
import org.cotrix.repository.codelist.CodelistSummary;
import org.cotrix.repository.impl.memory.MCriterion;
import org.cotrix.repository.impl.memory.MMultiQuery;
import org.cotrix.repository.impl.memory.MQuery;
import org.cotrix.repository.impl.memory.MemoryRepository;

/**
 * A {@link CodelistQueryFactory} for {@link MMultiQuery}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class MCodelistQueryFactory implements CodelistQueryFactory {
	
	
	@Override
	public MultiQuery<Codelist,Codelist> allLists() {
		
		return new MMultiQuery<Codelist,Codelist>() {
			public Collection<? extends Codelist> executeOn(MemoryRepository<? extends Codelist> r) {
				return r.getAll();
			}
		};
	}
	
	@Override
	public MultiQuery<Codelist,Code> allCodes(final String codelistId) {
		return new  MMultiQuery<Codelist,Code>() {
			public Collection<Code> executeOn(MemoryRepository<? extends Codelist> r) {
				Collection<Code> codes = new ArrayList<Code>();
				for (Code c : r.lookup(codelistId).codes())
					codes.add(c);
				return codes;
			}
		};
	}
	
	@Override
	public MultiQuery<Codelist,CodelistCoordinates> allListCoordinates() {
		return new MMultiQuery<Codelist,CodelistCoordinates>() {
			public Collection<CodelistCoordinates> executeOn(MemoryRepository<? extends Codelist> r) {
				Collection<CodelistCoordinates> coordinates = new HashSet<CodelistCoordinates>();
				for (Codelist list : r.getAll())
					coordinates.add(coords(list.id(),list.name(),list.version()));
				return coordinates;
			}
		};
	}
	
	@Override
	public Criterion<Codelist> byCodelistName() {
		
		return byName();
	}
	
	@Override
	public Criterion<Code> byCodeName() {
		
		return byName();
	}
	
	private static <T extends Named> Criterion<T> byName() {
		
		return new MCriterion<T>() {
			
			public int compare(T o1, T o2) {
				return o1.name().getLocalPart().compareTo(o2.name().getLocalPart());
			};
		};
	}
	
	
	public Query<Codelist,CodelistSummary> summary(final String id) {
		
		return new MQuery<Codelist, CodelistSummary>() {
			@Override
			public CodelistSummary execute(MemoryRepository<? extends Codelist> repository) {
				
				Codelist list = repository.lookup(id);

				if (list == null)
					throw new IllegalStateException("no such codelist: " + id);

				int size = list.codes().size();

				Collection<Attribute> attributes = new ArrayList<Attribute>();
				for (Attribute a : list.attributes())
					if (!a.type().equals(SYSTEM_TYPE))
						attributes.add(a);
				
				Map<QName,Map<QName,Set<String>>> fingerprint = new HashMap<QName, Map<QName,Set<String>>>();
				
				for (Code c : list.codes())
					addToFingerprint(fingerprint,c.attributes());
				
				return new CodelistSummary(list.name(), size, attributes, fingerprint);
			}
		};
	}
}
