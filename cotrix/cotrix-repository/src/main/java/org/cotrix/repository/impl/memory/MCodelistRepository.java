package org.cotrix.repository.impl.memory;

import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.repository.CodelistCoordinates.*;
import static org.cotrix.repository.CodelistSummary.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.namespace.QName;

import org.cotrix.common.Utils;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.impl.CodelistQueryFactory;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 * 
 */
@ApplicationScoped
public class MCodelistRepository extends MemoryRepository<Codelist.State> implements CodelistQueryFactory {

	
	@Override
	public MultiQuery<Codelist, Codelist> allLists() {

		return new MMultiQuery<Codelist, Codelist>() {
		
			Collection<? extends Codelist> _execute() {
				return adapt(getAll());
			}
			
		};
	}
	
	@Override
	public MultiQuery<Codelist, Code> allCodes(final String codelistId) {
		
		return new MMultiQuery<Codelist, Code>() {
			
			public Collection<? extends Code> _execute() {
				
				Collection<Code.State> codes = new ArrayList<Code.State>();
				
				for (Code.State c : lookup(codelistId).codes())
						codes.add(c);
				
				return adapt(codes);
			}
		};
	}

	@Override
	public MultiQuery<Codelist, CodelistCoordinates> allListCoordinates() {
		
		return new MMultiQuery<Codelist,CodelistCoordinates>() {
			
			public Collection<CodelistCoordinates> _execute() {
				Collection<CodelistCoordinates> coordinates = new HashSet<CodelistCoordinates>();
				for (Codelist.State list : getAll())
					coordinates.add(coordsOf(list));
				return coordinates;
			}
		};
		
	}

	@Override
	public MultiQuery<Codelist, CodelistCoordinates> codelistsFor(User u) {

		final FingerPrint fp = u.fingerprint();

		return new MMultiQuery<Codelist, CodelistCoordinates>() {
			
			public Collection<CodelistCoordinates> _execute() {

				Collection<CodelistCoordinates> coordinates = new HashSet<CodelistCoordinates>();
				for (Codelist.State list : getAll())
					if (!fp.allRolesOver(list.id(), codelists).isEmpty())
						coordinates.add(coordsOf(list));

				return coordinates;
			}
		};
	}
	

	public Query<Codelist, CodelistSummary> summary(final String id) {

		return new MQuery<Codelist, CodelistSummary>() {
		
			@Override
			public CodelistSummary execute() {

				Codelist.State state = lookup(id);

				if (state == null)
					throw new IllegalStateException("no such codelist: " + id);

				int size = state.codes().size();

				Codelist list = state.entity();
				
				Collection<Attribute> attributes = new ArrayList<Attribute>();
				
				for (Attribute a : list.attributes())
					if (!a.type().equals(SYSTEM_TYPE))
						attributes.add(a);

				Map<QName, Map<QName, Set<String>>> fingerprint = new HashMap<QName, Map<QName, Set<String>>>();

				for (Code c : list.codes())
					addToFingerprint(fingerprint, c.attributes());

				return new CodelistSummary(list.name(), size, attributes, fingerprint);
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
	public Criterion<Code> byAttribute(final Attribute attribute, final int position) {

		valid("attribute name", attribute.name());

		return new MCriterion<Code>() {

			private boolean matches(Attribute a) {

				return attribute.name().equals(a.name())
						&& (attribute.language() == null ? true : attribute.language().equals(a.language()));
			}

			@Override
			public int compare(Code c1, Code c2) {

				int pos = 1;
				String c1Match = null;
				for (Attribute a : c1.attributes())
					if (matches(a))
						if (pos == position) {
							c1Match = a.value();
							break;
						} else
							pos++;

				pos = 1;
				String c2Match = null;
				for (Attribute a : c2.attributes())
					if (matches(a))
						if (pos == position) {
							c2Match = a.value();
							break;
						} else
							pos++;

				if (c1Match == null)
					return c2Match == null ? 0 : 1;

				else
					return c2Match == null ? -1 : c1Match.compareTo(c2Match);
			}
		};

	}

	// helper
	@SuppressWarnings("all")
	private static <R> MCriterion<R> reveal(Criterion<R> criterion) {
		return Utils.reveal(criterion, MCriterion.class);
	}
}
