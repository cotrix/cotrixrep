package org.cotrix.repository.impl.memory;

import static java.lang.Math.*;
import static java.lang.Thread.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.repository.CodelistCoordinates.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.common.Utils;
import org.cotrix.common.async.TaskContext;
import org.cotrix.common.async.TaskUpdate;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.Codelist.State;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.memory.CodelistLinkMS;
import org.cotrix.domain.memory.DefinitionMS;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.UpdateAction;
import org.cotrix.repository.spi.CodelistActionFactory;
import org.cotrix.repository.spi.CodelistQueryFactory;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 * 
 */
@ApplicationScoped @Alternative @Priority(DEFAULT)
public class MCodelistRepository extends MemoryRepository<Codelist.State> implements CodelistQueryFactory, CodelistActionFactory {

	@Inject
	TaskContext context;
	
	@Override
	public void add(State list) {
		
		//simulate progress
		int total = list.codes().size()+list.definitions().size()+list.links().size()+list.attributes().size();
		int timeUnit = 1000/total;
		int progressInterval = min(100,total);
		try {
			
			int i;
			for (i=0; i < total;i = i +min(progressInterval,total-i)) {
				int step = min(progressInterval,total-i);
				sleep(step*timeUnit);
				context.save(new TaskUpdate(((float)i+step)/total, "loaded "+i+" of "+total+" elements"));
			}
				
			
		}
		catch(Exception e) {
			rethrowUnchecked(e);
		}
				
		super.add(list);
	}
	
	@Override
	public void remove(String id) {
		
		notNull("identifier", id);
		
		for (Codelist.State list : getAll())
			for (CodelistLink.State link : list.links())
				if (link.target().id().equals(id))
					throw new CodelistRepository.UnremovableCodelistException("cannot remove codelist "+list.id()+": others depend on it");
				
		super.remove(id);
	}
	
	
	
	
	// actions
	
	@Override
	public UpdateAction<Codelist> deleteDefinition(final String definitionId) {
		
		return new UpdateAction<Codelist>() {
			@Override
			public void performOver(Codelist list) {
				
				if (!list.definitions().contains(definitionId))
					throw new IllegalArgumentException("no attribute definition "+definitionId+" in list "+list.id()+" ("+list.name()+")");
				
					
				Definition def = list.definitions().lookup(definitionId);
				
				for (Code code : list.codes()) {
					Collection<Attribute> changesets = new ArrayList<>(); 
					for (Attribute a : code.attributes())
						if(a.definition().id().equals(def.id()))
							changesets.add(delete(a));
					
					reveal(code).update(reveal(modify(code).attributes(changesets).build()));
				}
				
				Definition changeset = new DefinitionMS(def.id(),DELETED).entity();
				
				reveal(list).update(reveal(modify(list).definitions(changeset).build()));
				
			}
			
			public String toString() {
				return "action [delete definition "+definitionId;
			}
		};
	}
	

	@Override
	public UpdateAction<Codelist> deleteCodelistLink(final String linkId) {
		
		return new UpdateAction<Codelist>() {
			@Override
			public void performOver(Codelist list) {
				
				if (!list.links().contains(linkId))
					throw new IllegalArgumentException("no link definition "+linkId+" in list "+list.id()+" ("+list.name()+")");
				
					
				CodelistLink type= list.links().lookup(linkId);
				
				for (Code code : list.codes()) {
					Collection<Codelink> changesets = new ArrayList<>(); 
					for (Codelink l : code.links())
						if(l.type().id().equals(type.id()))
							changesets.add(delete(l));
					
					reveal(code).update(reveal(modify(code).links(changesets).build()));
				}
				
				CodelistLink changeset = new CodelistLinkMS(type.id(),DELETED).entity();
				
				reveal(list).update(reveal(modify(list).links(changeset).build()));
				
			}
			
			public String toString() {
				return "action [delete definition "+linkId;
			}
		};
	}
	
	
	
	
	
	
	
	
	///queries
	
	@Override
	public MultiQuery<Codelist, Codelist> allLists() {

		return new MMultiQuery<Codelist, Codelist>() {
		
			Collection<Codelist> executeInMemory() {
				return adapt(getAll());
			}
			
		};
	}
	
	@Override
	public MultiQuery<Codelist, Code> allCodes(final String codelistId) {
		
		return new MMultiQuery<Codelist, Code>() {
			
			public Collection<Code> executeInMemory() {
				
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
			
			public Collection<CodelistCoordinates> executeInMemory() {
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
			
			public Collection<CodelistCoordinates> executeInMemory() {

				Collection<CodelistCoordinates> coordinates = new HashSet<CodelistCoordinates>();
				
				for (Codelist.State list : getAll())
					if (!fp.allRolesOver(list.id(), codelists).isEmpty())
						coordinates.add(coordsOf(list));

				return coordinates;
			}
		};
	}
	

	public Query<Codelist, CodelistSummary> summary(final String id) {

		return new Query.Private<Codelist, CodelistSummary>() {
		
			@Override
			public CodelistSummary execute() {

				Codelist.State state = lookup(id);

				if (state == null)
					throw new IllegalStateException("no such codelist: " + id);

				Codelist list = state.entity();
				
				return new CodelistSummary(list);
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
	public Criterion<Code> byAttribute(final Attribute template, final int position) {

		valid("attribute name", template.name());

		return new MCriterion<Code>() {

			private boolean matches(Attribute a) {

				return template.name().equals(a.name())
						&& (template.language() == null ? true : template.language().equals(a.language()));
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
	
	@Override
	public Criterion<Code> byLink(final CodelistLink template, final int position) {

		valid("link name", template.name());

		return new MCriterion<Code>() {

			private boolean matches(Codelink link) {

				return link.name().equals(template.name())
						&& link.type().equals(template);
			}

			@Override
			public int compare(Code c1, Code c2) {

				int pos = 1;
				List<Object> c1Match = null;
				for (Codelink link : c1.links())
					if (matches(link))
						if (pos == position) {
							c1Match = link.value();
							break;
						} else
							pos++;

				pos = 1;
				List<Object> c2Match = null;
				for (Codelink link : c2.links())
					if (matches(link))
						if (pos == position) {
							c2Match = link.value();
							break;
						} else
							pos++;

				if (c1Match == null)
					return c2Match == null ? 0 : 1;

				
				if (c2Match == null)
					return -1 ;
				else {
					//compare items orderly
					for (int i=0; i< min(c1Match.size(),c2Match.size()) ; i++) {
						Object o1 = c1Match.get(i);
						Object o2 = c2Match.get(i);
						//if comparable, compare. otherwise, move to next
						if (o1 instanceof Comparable) {
							@SuppressWarnings("all")
							int result = Comparable.class.cast(o1).compareTo(o2);
							if (result!=0)
								return result;
						}
					}
					
					//still a tie? prevails the longer of the two, if one is. otherwise, arbitrary.
					return c1Match.size()<c2Match.size()? -1 : 1;
				}
				
			}
		};

	}

	// helper
	@SuppressWarnings("all")
	private static <R> MCriterion<R> revealCriterion(Criterion<R> criterion) {
		return Utils.reveal(criterion, MCriterion.class);
	}
	
}
