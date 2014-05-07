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
import javax.inject.Singleton;
import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.IteratorAdapter;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.links.ValueType;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.User;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.NeoCode;
import org.cotrix.neo.domain.NeoCodelist;
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
public class NeoCodelistQueries extends NeoQueries implements CodelistQueryFactory {

	@Override
	public MultiQuery<Codelist, Codelist> allLists() {

		return new NeoMultiQuery<Codelist, Codelist>(engine) {

			{
				match(format("(%1$s:%2$s)",$node,CODELIST.name()));
				rtrn(format("%1$s as %2$s",$node,$result));
				
			}
			
			@Override
			public Iterator<Codelist> iterator() {

				
				ExecutionResult result = executeNeo();

				ResourceIterator<Node> it = result.columnAs($result);

				return codelists(it);
			}

		};
	}

	@Override
	public MultiQuery<Codelist, CodelistCoordinates> allListCoordinates() {

		return new NeoMultiQuery<Codelist, CodelistCoordinates>(engine) {
			
			{
				match(format("(%1$s:%2$s)",$node,CODELIST.name()));
				rtrn(format("%1$s as %2$s",$node,$result));
			}
			
			@Override
			public Iterator<CodelistCoordinates> iterator() {

				
				
				ExecutionResult result = executeNeo();

				ResourceIterator<Node> it = result.columnAs($result);

				return coordinates(it);
			}

		};
	}

	@Override
	public MultiQuery<Codelist, Code> allCodes(final String codelistId) {

		return new NeoMultiQuery<Codelist, Code>(engine) {

			{
				
				match(format("(L:%1$s {%2$s:'%3$s'})-[:%4$s]->(%5$s)",
						CODELIST.name(),
						id_prop,
						codelistId,
						Relations.CODE.name(),
						$node));
				
				rtrn(format("DISTINCT(%1$s) as %2$s",$node,$result));	
			}
			
			@Override
			public Iterator<Code> iterator() {

				
	
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

			{
				match(format("(%1$s:%2$s)",$node,CODELIST.name()));
				rtrn(format("%1$s as %2$s",$node,$result));
			}
			
			@Override
			public Iterator<CodelistCoordinates> iterator() {

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
				
				Collection<CodelistLink> links = new ArrayList<CodelistLink>();
				
				for (CodelistLink l : list.links())
					links.add(l);

				Map<QName, Map<QName, Set<String>>> fingerprint = new HashMap<QName, Map<QName, Set<String>>>();

				for (Code c : list.codes())
					addAttributesToFingerprint(fingerprint, c.attributes());

				return new CodelistSummary(list.name(), size, attributes, links, fingerprint);
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
	public Criterion<Codelist> byVersion() {
		
		return new NeoCriterion<Codelist>() {
			
			@Override
			protected String process(NeoMultiQuery<?, ?> query) {
				return format("%1$s.%2$s",$node,version_prop);
			}
		};
	}

	@Override
	public Criterion<Code> byAttribute(final Attribute template, final int position) {
		
		return new NeoCriterion<Code>() {
			
			@Override
			protected String process(NeoMultiQuery<?,?> query) {
				
				String $attribute = "A";
				
				//add match for attributes
				query.match(format("%1$s-[:%2$s]->(%3$s)",$node,Relations.ATTRIBUTE.name(),$attribute));
				
				//brings codes in with expression
				query.with($node);
				
				String caseTemplate = "CASE %1$s WHEN '%2$s' THEN %3$s END AS VAL ORDER BY VAL";
				
				String expression = template.language()==null?
						format("%1$s.%2$s[%3$s]",$attribute,name_prop,position-1):
						format("%1$s.%2$s[%3$s]+%1$s.%4$s",$attribute,name_prop,position-1,lang_prop);
				
				String value = template.language()==null?
										template.name().toString():
										template.name()+template.language();
										
				String caseValue = format("%1$s.%2$s",$attribute,value_prop);
				
				query.with(format(caseTemplate,expression,value,caseValue));
				
				return "";
			}
		};
	}
	
	@Override
	public Criterion<Code> byLink(final CodelistLink template, final int position) {
		
		return new NeoCriterion<Code>() {
			
			@Override
			protected String process(NeoMultiQuery<?,?> query) {
				
				query.match(format("%s-[:%s]->(L)",$node,Relations.LINK.name(),Relations.INSTANCEOF.name()));
				query.match(format("L-[:%s]->(LT)",position-1,Relations.INSTANCEOF.name()));
				query.match(format("%s-[:%s]->(C)",Relations.LINK));
				
				//brings codes in with expression
				query.with($node);
				
				String caseTemplate = "CASE %s WHEN '%s' THEN %s END AS VAL ORDER BY VAL";
				
				String expression = format("LT.%s",name_prop,position-1);
				
				String value = template.name().toString();
				
				String caseValue = format("C.%s",name_prop);
				
				query.with(format(caseTemplate,expression,value,caseValue));

				
				return "";
			}
		};
	}

	
	
	
	// helpers
	
	Iterator<Codelist> codelists(ResourceIterator<Node> it) {
		return new IteratorAdapter<>(new NeoNodeIterator<>(it, NeoCodelist.factory));
	}

	
	Iterator<Code> codes(ResourceIterator<Node> it) {
		return new IteratorAdapter<>(new NeoNodeIterator<>(it, NeoCode.factory));
	}

	
}
