package org.cotrix.neo.repository;

import static java.lang.String.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.Constants.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.cotrix.repository.CodelistCoordinates.*;
import static org.cotrix.repository.CodelistSummary.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.namespace.QName;

import org.cotrix.common.Utils;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.IteratorAdapter;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.User;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.NeoCode;
import org.cotrix.neo.domain.NeoCodelist;
import org.cotrix.neo.domain.utils.NeoBeanFactory;
import org.cotrix.neo.domain.utils.NeoNodeIterator;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.spi.CodelistQueryFactory;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

@Singleton
@Alternative
@Priority(RUNTIME)
public class NeoCodelistQueries implements CodelistQueryFactory {

	public static final String $node = "N";
	private static final String $result = "RESULT";

	@Inject
	private NeoQueryEngine engine;
	
	public Query.Private<Codelist, Integer> repositorySize() {

		return new Query.Private<Codelist, Integer>() {

			@Override
			public Integer execute() {

				String query = format("MATCH (%1$s:%2$s) RETURN COUNT(%1$s) as %3$s", 
									  $node, 
									  CODELIST.name(),
									  $result);

				ExecutionResult result = engine.execute(query);

				try (ResourceIterator<Long> it = result.columnAs($result);) {
					return it.next().intValue();
				}
			}
		};
	}

	@Override
	public MultiQuery<Codelist, Codelist> allLists() {

		return new NeoMultiQuery<Codelist, Codelist>(engine) {

			@Override
			public Iterator<Codelist> iterator() {

				match(format("(%1$s:%2$s)",$node,CODELIST.name()));
				rtrn(format("%1$s as %2$s",$node,$result));
				
				ExecutionResult result = executeNeo();

				ResourceIterator<Node> it = result.columnAs($result);

				return codelists(it);
			}

		};
	}

	@Override
	public MultiQuery<Codelist, CodelistCoordinates> allListCoordinates() {

		return new NeoMultiQuery<Codelist, CodelistCoordinates>(engine) {

			@Override
			public Iterator<CodelistCoordinates> iterator() {

				match(format("(%1$s:%2$s)",$node,CODELIST.name()));
				rtrn(format("%1$s as %2$s",$node,$result));
				
				ExecutionResult result = executeNeo();

				ResourceIterator<Node> it = result.columnAs($result);

				return coordinates(it);
			}

		};
	}

	@Override
	public MultiQuery<Codelist, Code> allCodes(final String codelistId) {

		return new NeoMultiQuery<Codelist, Code>(engine) {

			@Override
			public Iterator<Code> iterator() {

				match(format("(L:%1$s {%2$s:'%3$s'})-[:%4$s]->(%5$s)",
						CODELIST.name(),
						id_prop,
						codelistId,
						Relations.CODE.name(),
						$node));
				
				rtrn(format("DISTINCT(%1$s) as %2$s",$node,$result));
	
				ExecutionResult result = executeNeo();

				//System.out.println(result.dumpToString());
				
				ResourceIterator<Node> it = result.columnAs($result);

				return codes(it);
			}

		};
	}

	@Override
	public MultiQuery<Codelist, CodelistCoordinates> codelistsFor(User u) {
		
		final FingerPrint fp = u.fingerprint();
		
		return new NeoMultiQuery<Codelist, CodelistCoordinates>(engine) {

			@Override
			public Iterator<CodelistCoordinates> iterator() {

				match(format("(%1$s:%2$s)",$node,CODELIST.name()));
				
				rtrn(format("%1$s as %2$s",$node,$result));
				
				ExecutionResult result = executeNeo();

				//System.out.println(result.dumpToString());
				
				Collection<CodelistCoordinates> coordinates = new HashSet<CodelistCoordinates>();
				
				try (
					ResourceIterator<Node> it = result.columnAs($result);
				) 
				{
					while (it.hasNext()) {
						NeoCodelist state = new NeoCodelist(it.next());
						if (!fp.allRolesOver(state.id(), codelists).isEmpty())
								coordinates.add(coordsOf(state));
					}
						
				}
			
				return coordinates.iterator();
			}
			
		};
			
	}

	@Override
	public Query<Codelist, CodelistSummary> summary(final String codelistId) {
		
		return new Query.Private<Codelist, CodelistSummary>() {

			@Override
			public CodelistSummary execute() {

				String query = format("MATCH (%1$s:%2$s {%3$s:'%4$s'}) RETURN %1$s as %5$s", 
									  $node, 
									  CODELIST.name(),
									  id_prop,
									  codelistId,
									  $result);

				ExecutionResult result = engine.execute(query);

				NeoCodelist state = null;
				
				try (ResourceIterator<Node> it = result.columnAs($result)) {
					
					if (!it.hasNext())
						throw new IllegalStateException("no such codelist: " + codelistId);

					state = new NeoCodelist(it.next());
				}
				
				
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
		return new NeoCriterion<Codelist>() {
			
			@Override
			protected String process(NeoMultiQuery<?, ?> query) {
				return format("%1$s.%2$s",$node,name_prop);
			}
		};
	}

	@Override
	public Criterion<Code> byCodeName() {
		
		return new NeoCriterion<Code>() {
			
			@Override
			protected String process(NeoMultiQuery<?, ?> query) {
				return format("%1$s.%2$s",$node,name_prop);
			}
		};
		
	}

	@Override
	@SuppressWarnings("all")
	public Criterion<CodelistCoordinates> byCoordinateName() {
		return (Criterion) byCodelistName();
	}

	@Override
	public <T> Criterion<T> all(final Criterion<T> c1, final Criterion<T> c2) {
		
		return new NeoCriterion<T>() {
			
			@Override
			protected String process(NeoMultiQuery<?, ?> query) {
				return format("%1$s,%2$s",reveal(c1).process(query),reveal(c2).process(query));
			}
		};
		
	}

	@Override
	public <T> Criterion<T> descending(final Criterion<T> c) {

		return new NeoCriterion<T>() {
			
			@Override
			protected String process(NeoMultiQuery<?, ?> query) {
				return format("%1$s DESC",reveal(c).process(query));
			}
		};
	}

	@Override
	public Criterion<Codelist> byVersion() {
		
		return new NeoCriterion<Codelist>() {
			
			@Override
			protected String process(NeoMultiQuery<?, ?> query) {
				return format("%1$s.%2$s",$node,version_prop);
			}
		};
	}

	@Override
	public Criterion<Code> byAttribute(final Attribute attribute, final int position) {
		
		return new NeoCriterion<Code>() {
			
			@Override
			protected String process(NeoMultiQuery<?,?> query) {
				
				String $attribute = "A";
				
				//add match for attributes
				query.match(format("%1$s-[:%2$s]->(%3$s)",$node,Relations.ATTRIBUTE.name(),$attribute));
				
				//brings codes in with expression
				query.with($node);
				
				String caseTemplate = "CASE %1$s WHEN '%2$s' THEN %3$s END AS VAL ORDER BY VAL";
				
				String expression = attribute.language()==null?
						format("%1$s.%2$s[%3$s]",$attribute,name_prop,position-1):
						format("%1$s.%2$s[%3$s]+%1$s.%4$s",$attribute,name_prop,position-1,lang_prop);
				
				String value = attribute.language()==null?
										attribute.name().toString():
										attribute.name()+attribute.language();
										
				String caseValue = format("%1$s.%2$s",$attribute,value_prop);
				
				query.with(format(caseTemplate,expression,value,caseValue));
				
				return "";
			}
		};
	}

	// helpers
	
	NeoCriterion<?> reveal(Criterion<?> c) {
		return Utils.reveal(c,NeoCriterion.class);
	}

	Iterator<Codelist> codelists(ResourceIterator<Node> it) {
		return new IteratorAdapter<>(new NeoNodeIterator<>(it, NeoCodelist.factory));
	}

	
	Iterator<Code> codes(ResourceIterator<Node> it) {
		return new IteratorAdapter<>(new NeoNodeIterator<>(it, NeoCode.factory));
	}

	
	Iterator<CodelistCoordinates> coordinates(ResourceIterator<Node> it) {

		NeoBeanFactory<CodelistCoordinates> factory = new NeoBeanFactory<CodelistCoordinates>() {

			public CodelistCoordinates beanFrom(Node node) {

				NeoCodelist list = new NeoCodelist(node);

				return new CodelistCoordinates(list.id(), list.name(), list.version().value());

			};
		};

		return new NeoNodeIterator<>(it, factory);
	}

}
