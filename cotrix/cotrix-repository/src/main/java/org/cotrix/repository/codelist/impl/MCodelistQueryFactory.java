package org.cotrix.repository.codelist.impl;

import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.Utils.*;
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

import org.cotrix.common.Utils;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.User;
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
					coordinates.add(coordsOf(list));
				return coordinates;
			}
		};
	}
	
	@Override
	public MultiQuery<Codelist, CodelistCoordinates> codelistsFor(User u) {
		
		final FingerPrint fp = u.fingerprint();
		
		return new MMultiQuery<Codelist,CodelistCoordinates>() {
			public Collection<CodelistCoordinates> executeOn(MemoryRepository<? extends Codelist> r) {
				
				Collection<CodelistCoordinates> coordinates = new HashSet<CodelistCoordinates>();
				for (Codelist list : r.getAll())
					if (!fp.allRolesOver(list.id(),codelists).isEmpty())
						coordinates.add(coordsOf(list));
				
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
	
	@Override
	public Criterion<CodelistCoordinates> byCoordinateName() {
		
		return byName();
	}
	
	private <T extends Named> Criterion<T> byName() {
		
		return new MCriterion<T>() {
			
			public int compare(T o1, T o2) {
				return o1.name().getLocalPart().compareTo(o2.name().getLocalPart());
			};
		};
	}
	
	@Override
	public Criterion<Codelist> byVersion() {
		
		return new MCriterion<Codelist>() {
			
			public int compare(Codelist o1, Codelist o2) {
				return o1.version().compareTo(o2.version());
			};
		};
		
	}
	
	@Override
	public <T> Criterion<T> all(final Criterion<T> c1, final Criterion<T> c2) {
		
		return new MCriterion<T>() {
			
			public int compare(T o1, T o2) {
				int result = reveal(c1).compare(o1, o2);
				
				if (result==0)
					result = reveal(c2).compare(o1, o2);
				
				return result;
			};
			
		};
	}
	
	@Override
	public Criterion<Code> byAttribute(final Attribute attribute, final int position) {
		
		valid("attribute name",attribute.name());
		
		return new MCriterion<Code>() {
			
			private boolean matches(Attribute a) {
				
				return 
						attribute.name().equals(a.name()) &&
						(attribute.language()==null ?
								true :
								attribute.language().equals(a.language()));
			}
			
			@Override
			public int compare(Code c1, Code c2) {
				
				int pos = 1;
				String c1Match = null;
				for (Attribute a : c1.attributes())
					if (matches(a))
						if (pos==position) {
							c1Match = a.value();
							break;
						}
						else
							pos++;
				
				pos = 1;
				String c2Match = null;
				for (Attribute a : c2.attributes())
					if (matches(a))
						if (pos==position) {
							c2Match = a.value();
							break;
						}
						else
							pos++;
				
				
				if (c1Match==null)
					return c2Match==null? 0: 1;
				
				else
					return c2Match==null? -1: c1Match.compareTo(c2Match);
			}
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
	
	
	//helper
	@SuppressWarnings("all")
	private static <R> MCriterion<R> reveal(Criterion<R> criterion) {
		return Utils.reveal(criterion, MCriterion.class);
	}
}
